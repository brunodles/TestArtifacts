package com.brunodles.testartifacts.task

import com.brunodles.testartifacts.TestArtifactsExtension
import com.brunodles.testartifacts.TestArtifactsPlugin
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4.class)
class MergeTestArtifactsTest {

    private Project project

    @Before
    def setupProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply TestArtifactsPlugin
        (project.extensions.archiver as TestArtifactsExtension).with {
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
    }

    @Test
    void withoutSetup_extensionShouldBeEmpty() {
        def archiver = project.extensions.archiver
        Assert.fail("WIP")
    }
}
