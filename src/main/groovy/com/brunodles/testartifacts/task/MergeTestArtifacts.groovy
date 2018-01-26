package com.brunodles.testartifacts.task

import com.brunodles.testartifacts.TestArtifactsExtension
import com.brunodles.testartifacts.TestArtifactsPlugin
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

        Map<?, ?> result = mergeReports(extension)
        result.put('totals', totals(result))
        result.put('buildInfo', buildInfo(extension))

        writeReport(result)
    }

    private Map<?, ?> mergeReports(TestArtifactsExtension extension) {
        def files = extension.files ?: new HashMap<String, List<String>>()
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
        result
    }

    private static Map<String, String> buildInfo(TestArtifactsExtension extension) {
        return [
                projectName: extension.projectName,
                buildNumber: extension.buildNumber ?: 'debug',
                moduleName : extension.moduleName,
                dateTime   : new SimpleDateFormat("yyyy.MM.dd HH:MM:ss").format(new Date())
        ]
    }

    private static Map totals(Map json) {
        Map<String, Map<String, Double>> total = [
                checkstyle: [
                        errors: 0D
                ],
                test      : [
                        errors     : 0D, // All errors, only junit < 4
                        tests      : 0D, // All tests completed. error, skipped and failures are included
                        failures   : 0D, // assertion erros
                        skipped    : 0D, // ignored tests
                        successRate: 0D
                ],
                coverage  : [
                        covered    : 0D,
                        missed     : 0D,
                        coveredRate: 0D
                ]
        ]
        json.each { type, file ->
            if ("buildInfo" == type)
                return
            file.each { reportName, Map data ->
                if (type == "checkstyle")
                    total.checkstyle.errors += countCheckstyleErrors(data)
                if (type == 'test') {
                    total.test.errors += Integer.valueOf(data.errors.toString()) ?: 0
                    total.test.failures += Integer.valueOf(data.failures.toString())
                    total.test.skipped += Integer.valueOf(data.skipped.toString())
                    total.test.tests += Integer.valueOf(data.tests.toString())
                }
                if (type == 'jacoco') {
                    data.get('counterList').each { counterData ->
                        if ("INSTRUCTION" == counterData.type) {
                            total.coverage.missed += Integer.valueOf(counterData.missed.toString())
                            total.coverage.covered += Integer.valueOf(counterData.covered.toString())
                        }
                    }

                }
            }
        }
        def instructions = (total.coverage.covered + total.coverage.missed)
        if (instructions > 0)
            total.coverage.coveredRate = Math.round(total.coverage.covered * 10000 / instructions) / 100
        total.test.success = total.test.tests - (total.test.errors + total.test.failures + total.test.skipped)
        total.test.successRate = Math.round(total.test.success * 10000 / total.test.tests) / 100
        return total
    }

    private static int countCheckstyleErrors(Map fileData) {
        if (!fileData.containsKey("fileList"))
            return 0
        int count = 0
        fileData.get('fileList').each {
            it.each { k, v ->
                if ("errorList" == k)
                    count += v.size
            }
        }
        return count
    }

    private void writeReport(Map<?, ?> result) {
        def json = new JsonBuilder(result).toString()
        def file = getOutputJson()
        println "Saving at '$file.path'."
        file.write(json)
    }

}
