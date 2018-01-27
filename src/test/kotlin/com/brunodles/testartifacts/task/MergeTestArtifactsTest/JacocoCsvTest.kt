package com.brunodles.testartifacts.task.MergeTestArtifactsTest

import com.brunodles.testing.Assertions
import com.brunodles.testing.Resources
import com.brunodles.testing.withJacoco
import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File

@RunWith(JUnit4::class)
class JacocoCsvTest {

    private
    val JACOCO_FILE = Resources.readResource("MergeTestArtifacts/jacoco.csv")

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
                        'jacocoCsv' : ['jacocoTestReport.csv']
                ]
            }
        """)
        val buildFolder = testProjectDir.newFolder("build")
        val file = File(buildFolder, "jacocoTestReport.csv")
        file.createNewFile()
        file.writeText(JACOCO_FILE)
    }

    @Test
    fun shouldAddJacocoCsvData() {
        GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments("mergeTestArtifacts")
                .withPluginClasspath()
                .withJacoco()
                .build()
        val reports = File(testProjectDir.root, "build/reports/uploadReports.json")
        val expected = Resources.readResource("MergeTestArtifacts/jacococsv_output")
        Assertions.assertMatches(expected, reports.readText())
    }
}
