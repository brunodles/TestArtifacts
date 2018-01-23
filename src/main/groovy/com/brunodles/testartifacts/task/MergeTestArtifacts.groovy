package com.brunodles.testartifacts.task

import com.brunodles.testartifacts.FileTypes
import com.brunodles.testartifacts.TestArtifactsExtension
import com.brunodles.testartifacts.TestArtifactsPlugin
import groovy.json.JsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import java.text.SimpleDateFormat

import static com.brunodles.testartifacts.StringUtils.*

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
        def files = extension.files ?: new HashMap()
        def projectName = extension.projectName
        def moduleName = extension.moduleName
        def buildNumber = extension.buildNumber

        def parser = new XmlParser(false, false, false)
        def result = files.collectEntries { type, path ->
            def result = path.collect { project.fileTree(dir: project.buildDir, includes: [it]).asList() }
                    .flatten()
                    .findAll { it.exists() }
                    .collectEntries { file ->
                if (FileTypes.valueOf(type).decoder == 'xml') {
                    String text = file.text.replaceAll("\\<\\!?DOCTYPE.*?\\>", "")
                    def jsonRoot = nested(parser.parseText(text))
                    return [(fixKeyIfNeeded("${file.name}")): jsonRoot]
                } else {
                    boolean header = true
                    def fieldNames = []
                    def jsonRoot = new HashMap()
                    file.splitEachLine(",") { fields ->
                        if (header) {
                            fieldNames = fields.collect { underscoreToCamelCase(it.toLowerCase()) }
                            header = false
                            return
                        }
                        def values = new HashMap()
                        for (int i = 0; i < fieldNames.size(); i++)
                            values.put(fieldNames.get(i), fields.get(i))
                        def key = "${fields.get(0)}_${fields.get(1)}"
                        //noinspection GroovyVariableNotAssigned
                        jsonRoot.put(fixKeyIfNeeded(key), values)
                    }
                    return [(fixKeyIfNeeded("${moduleName}_${file.name}")): jsonRoot]
                }
            }
            [(fixKeyIfNeeded(type)): result]
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
