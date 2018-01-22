package com.brunodles.testartifacts.task

import org.gradle.testkit.runner.GradleRunner
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

@RunWith(JUnit4.class)
class MergeTestArtifactsTest {

    @Rule
    public TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    @Before
    void setupProject() {
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
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
        """
    }

    @Test
    void shouldCreateMergeReportFile() {
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('mergeTestArtifacts')
                .withPluginClasspath()
                .build()
        Assert.assertTrue(result.output.contains("Saving at"))
        Assert.assertTrue(result.task(":mergeTestArtifacts").outcome == SUCCESS)
        def reports = new File(testProjectDir.root, 'build/reports/uploadReports.json')
        Assert.assertTrue(reports.exists())
    }
}
