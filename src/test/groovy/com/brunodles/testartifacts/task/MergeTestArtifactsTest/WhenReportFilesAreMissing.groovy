package com.brunodles.testartifacts.task.MergeTestArtifactsTest

import com.brunodles.testing.Assertions
import com.brunodles.testing.Resources
import org.gradle.testkit.runner.GradleRunner
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.util.regex.Pattern

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

@RunWith(JUnit4.class)
class WhenReportFilesAreMissing {

    private static final
    def EXPECTED_OUTPUT = Pattern.compile(Resources.readResource("MergeTestArtifacts/empty_output"))

    @Rule
    public TemporaryFolder testProjectDir = new TemporaryFolder()
    private File buildFile

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
    void shouldCreateMergeReportFile_withKeys() {
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('mergeTestArtifacts')
                .withPluginClasspath()
                .withJacoco()
        .withDebug(true)
                .build()
        Assert.assertTrue(result.output.contains("Saving at"))
        Assert.assertTrue(result.task(":mergeTestArtifacts").outcome == SUCCESS)
        def reports = new File(testProjectDir.root, 'build/reports/uploadReports.json')
        Assert.assertTrue(reports.exists())
        Assertions.assertMatches(EXPECTED_OUTPUT, reports.text)
    }
}
