package com.brunodles.testartifacts

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull

@RunWith(JUnit4.class)
class TestArtifactsExtensionTest {

    private Project project

    @Before
    void setupProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply TestArtifactsPlugin
    }

    @Test
    void withoutSetup_extensionShouldUseDefaultValues() {
        def archiver = project.extensions.archiver as TestArtifactsExtension
        assertNull archiver.files
        assertNull archiver.projectName
        assertNull archiver.moduleName
        assertNull archiver.buildNumber
        assertEquals 'test-artifacts', archiver.firebaseUrl
    }
}
