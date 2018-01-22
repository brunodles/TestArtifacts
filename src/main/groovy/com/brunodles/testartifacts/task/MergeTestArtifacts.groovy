package com.brunodles.testartifacts.task

import com.brunodles.testartifacts.FileTypes
import com.brunodles.testartifacts.TestArtifactsExtension
import com.brunodles.testartifacts.TestArtifactsPlugin
import groovy.json.JsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.text.SimpleDateFormat

import static com.brunodles.testartifacts.StringUtils.*

class MergeTestArtifacts extends DefaultTask {

    Map<String, List<String>> files
    String projectName
    String moduleName
    String buildNumber

    MergeTestArtifacts() {
        TestArtifactsExtension extension = project.getExtensions().findByName(TestArtifactsPlugin.EXTENSION) as TestArtifactsExtension
        files = extension.files
        projectName = extension.projectName
        moduleName = extension.moduleName
        buildNumber = extension.buildNumber
    }

    @TaskAction
    def merge() {
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
                dateTime   : new SimpleDateFormat("yyyy.MM.dd HH:MM:ss").format(new Date())
        ])
        def json = new JsonBuilder(result).toString()
        def file = new File(project.buildDir, "reports/uploadReports.json")
        println "Saving at '$file.path'."
        file.write(json)
    }

}
