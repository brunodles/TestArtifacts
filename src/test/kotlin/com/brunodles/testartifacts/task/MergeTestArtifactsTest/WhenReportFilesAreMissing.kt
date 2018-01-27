package com.brunodles.testartifacts.task.MergeTestArtifactsTest


import com.brunodles.testing.Assertions
import com.brunodles.testing.Resources
import com.brunodles.testing.withJacoco
import org.gradle.testkit.runner.GradleRunner
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.util.regex.Pattern

import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import java.io.File

@RunWith(JUnit4::class)
class WhenReportFilesAreMissing {

    private val EXPECTED_OUTPUT = Pattern.compile(Resources.readResource("MergeTestArtifacts/empty_output"))

    @Rule
    @JvmField
    val testProjectDir = TemporaryFolder()
    private lateinit var buildFile: File

    @Before
    fun setupProject() {
        buildFile = testProjectDir.newFile("build.gradle")
        buildFile.writeText("""
            plugins {
                id 'testartifacts' version '0.1.0'
            }
            archiver {
                projectName = "AnimeWatcher"
                moduleName = "Explorer"
                buildNumber = 123
                files = [
                        'checkstyle': ['ktlint.xml', 'reports/checkstyle/main.xml'],
                        'jacoco'    : ['reports/jacoco/test/jacocoTestReport.xml'],
                        'jacocoCsv' : ['reports/jacoco/test/jacocoTestReport.csv'],
                        'lint'      : ['lint-results.xml'],
                        'test'      : ['test-results/test/TEST**.xml']
                ]
            }
        """)
    }

    @Test
    fun shouldCreateMergeReportFile_withKeys() {
        val result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments("mergeTestArtifacts")
                .withPluginClasspath()
                .withJacoco()
                .withDebug(true)
                .build()
        Assert.assertTrue(result.output.contains("Saving at"))
        Assert.assertTrue(result.task(":mergeTestArtifacts")!!.outcome == SUCCESS)
        val reports = File(testProjectDir.root, "build/reports/uploadReports.json")
        Assert.assertTrue(reports.exists())
        Assertions.assertMatches(EXPECTED_OUTPUT, reports.readText())
    }
}
