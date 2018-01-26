package com.brunodles.testartifacts.task

import com.brunodles.testartifacts.TestArtifactsExtension
import com.brunodles.testartifacts.TestArtifactsPlugin
import com.brunodles.testartifacts.reportparser.ReportTotalizer
import com.brunodles.testartifacts.reportparser.ReportType
import groovy.json.JsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import java.text.SimpleDateFormat

import static com.brunodles.testartifacts.helpers.StringUtils.removeEspecialCharacters

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
    def buildReport() {
        TestArtifactsExtension extension = project.getExtensions().findByName(TestArtifactsPlugin.EXTENSION) as TestArtifactsExtension

        Map<String, Object> result = mergeReports(extension)
        result.put('totals', totals(result))
        result.put('buildInfo', buildInfo(extension))

        writeReport(result)
    }

    private Map<String, Object> mergeReports(TestArtifactsExtension extension) {
        def files = extension.files ?: new HashMap<String, List<String>>()
        return files.collectEntries { type, path ->
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
    }

    private static Map<String, String> buildInfo(TestArtifactsExtension extension) {
        return [
                projectName: extension.projectName,
                buildNumber: extension.buildNumber ?: 'debug',
                moduleName : extension.moduleName,
                dateTime   : new SimpleDateFormat("yyyy.MM.dd HH:MM:ss").format(new Date())
        ]
    }

    private static Map totals(Map<String, Object> json) {
        Map<String, ReportTotalizer> totalizers = ReportType.values().findAll { it.totalizerClass }.collectEntries {
            [(it.name()): it.newTotalizer()]
        }
        json.each { type, typeFiles ->
            if (totalizers.containsKey(type))
                typeFiles.each { String fileName, Map data ->
                    totalizers[type].onData(fileName, data)
                }
        }
        return totalizers.collectEntries { k, v ->
            [(k): v.result()]
        }
    }

    private void writeReport(Map<?, ?> result) {
        def json = new JsonBuilder(result).toString()
        def file = getOutputJson()
        println "Saving at '$file.path'."
        file.write(json)
    }

}
