package com.brunodles.testartifacts.task

import groovy.json.JsonSlurper
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

class Totals extends DefaultTask {

    @InputFile
    File file = new File(project.buildDir, "reports/uploadReports.json")

    @TaskAction
    def countTotals() {
        def slurper = new JsonSlurper()
        def total = [
                checkstyleErrors: 0, // Count of checkstyle errors
                test            : [
                        errors  : 0, // All errors, only junit < 4
                        tests   : 0, // All tests completed. error, skipped and failures are included
                        failures: 0, // assertion erros
                        skipped : 0  // ignored tests
                ]
        ]
        Map json = slurper.parse(file) as Map
        json.each { type, v ->
            v.each { reportName, Map data ->
                if (type == "checkstyle")
                    total.checkstyleErrors += countCheckstyleErrors(data)
                if (type == 'test') {
                    total.test.errors += Integer.valueOf(data.errors.toString()) ?: 0
                    total.test.failures += Integer.valueOf(data.failures.toString())
                    total.test.skipped += Integer.valueOf(data.skipped.toString())
                    total.test.tests += Integer.valueOf(data.tests.toString())
                }
            }
        }
        total.test.success = total.test.tests - (total.test.errors + total.test.failures + total.test.skipped)
        println total
    }

    static int countCheckstyleErrors(Map fileData) {
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
}
