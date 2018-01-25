package com.brunodles.testartifacts.task.MergeTestArtifactsTest

import com.brunodles.testing.Assertions
import com.brunodles.testing.Resources
import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4.class)
class JacocoCsvTest {

    private static final
    def JACOCO_FILE = Resources.readResource("MergeTestArtifacts/jacoco.csv")

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
                        'jacocoCsv' : ['jacocoTestReport.csv']
                ]
            }
        """
        def buildFolder = testProjectDir.newFolder("build")
        def file = new File(buildFolder, "jacocoTestReport.csv")
        file.createNewFile()
        file << JACOCO_FILE
    }

    @Test
    void shouldAddJacocoCsvData() {
        GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('mergeTestArtifacts')
                .withPluginClasspath()
                .withJacoco()
                .build()
        def reports = new File(testProjectDir.root, 'build/reports/uploadReports.json')
        def expected = Resources.readResource("MergeTestArtifacts/jacococsv_output")
        Assertions.assertMatches(expected, reports.text)
    }
}
