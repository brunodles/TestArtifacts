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
class JacocoXmlTest {

    private static final
    def JACOCO_FILE = Resources.readResource("MergeTestArtifacts/jacoco.xml")

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
                        'jacoco' : ['jacocoTestReport.xml']
                ]
            }
        """
        def buildFolder = testProjectDir.newFolder("build")
        def file = new File(buildFolder, "jacocoTestReport.xml")
        file.createNewFile()
        file << JACOCO_FILE
    }

    @Test
    void shouldAddJacocoXmlData() {
        GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('mergeTestArtifacts')
                .withPluginClasspath()
                .withJacoco()
                .forwardStdError(System.err.newWriter())
                .forwardStdOutput(System.out.newWriter())
                .forwardOutput()
                .withDebug(true)
                .build()
        def reports = new File(testProjectDir.root, 'build/reports/uploadReports.json')
        def expected = Resources.readResource("MergeTestArtifacts/jacocoxml_output")
        Assertions.assertMatches(expected, reports.text)
    }
}
