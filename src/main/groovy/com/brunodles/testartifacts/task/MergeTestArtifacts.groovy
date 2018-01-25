package com.brunodles.testartifacts.task

import com.brunodles.testartifacts.reportparser.ReportType
import com.brunodles.testartifacts.TestArtifactsExtension
import com.brunodles.testartifacts.TestArtifactsPlugin
import groovy.json.JsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import java.text.SimpleDateFormat

import static com.brunodles.testartifacts.helpers.StringUtils.*

class MergeTestArtifacts extends DefaultTask {

    @InputFiles
    def getReports() {
        TestArtifactsExtension extension = project.getExtensions().findByName(TestArtifactsPlugin.EXTENSION) as TestArtifactsExtension
        def files = extension.files ?: new HashMap()
        return files.collect { type, path ->
            path.collect { project.fileTree(dir: project.buildDir, include: it).asList() }
        }
    }

    @OutputFile
    def getOutputJson() {
        def reportDir = new File(project.buildDir, 'reports')
        reportDir.mkdirs()
        return new File(reportDir, "uploadReports.json")
    }

    @TaskAction
    def mergeReports() {
        TestArtifactsExtension extension = project.getExtensions().findByName(TestArtifactsPlugin.EXTENSION) as TestArtifactsExtension
        def files = extension.files ?: new HashMap<String, List<String>>()
        def projectName = extension.projectName
        def moduleName = extension.moduleName
        def buildNumber = extension.buildNumber

        def result = files.collectEntries { type, path ->
            def result = path.collect { project.fileTree(dir: project.buildDir, includes: [it]).asList() }
                    .flatten()
                    .findAll { it.exists() }
                    .collectEntries { file ->
                def reportType = ReportType.valueOf(type)
                if (file instanceof File)
                    return [(removeEspecialCharacters("${file.name}")): reportType.parse(file as File)]
                return null
            }
            [(removeEspecialCharacters(type)): result]
        }
        result.put('buildInfo', [
                projectName: projectName,
                buildNumber: buildNumber,
                moduleName : moduleName,
                dateTime   : new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date())
        ])
        def json = new JsonBuilder(result).toString()
        def file = getOutputJson()
        println "Saving at '$file.path'."
        file.write(json)
    }

}
