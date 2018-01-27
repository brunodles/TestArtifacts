package com.brunodles.testartifacts

import com.brunodles.testartifacts.task.MergeTestArtifacts
import com.brunodles.testartifacts.task.UploadTestArtifacts
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import org.junit.Assert.*

@RunWith(JUnit4::class)
class TestArtifactsPluginTest {

    private lateinit var project: Project

    @Before
    fun setupProject() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply(TestArtifactsPlugin::class.java)
    }

    @Test
    fun withoutSetup_extensionShouldUseDefaultValues() {
        val archiver = project.extensions.getByName("archiver") as TestArtifactsExtension
        assertTrue(archiver.files.isEmpty())
        assertNull(archiver.projectName)
        assertNull(archiver.moduleName)
        assertNull(archiver.buildNumber)
        assertEquals("test-artifacts", archiver.firebaseUrl)
    }

    @Test
    fun whenApplyPlugin_shouldAddTasksAndExtensions() {
        assertTrue(project.tasks.getByName("mergeTestArtifacts") is MergeTestArtifacts)
        assertTrue(project.tasks.getByName("uploadTestArtifacts") is UploadTestArtifacts)
        assertTrue(project.extensions.getByName("archiver") is TestArtifactsExtension)
    }
}
