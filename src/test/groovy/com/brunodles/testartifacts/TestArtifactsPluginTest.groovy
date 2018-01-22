package com.brunodles.testartifacts

import com.brunodles.testartifacts.task.MergeTestArtifacts
import com.brunodles.testartifacts.task.Totals
import com.brunodles.testartifacts.task.UploadTestArtifacts
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static org.junit.Assert.assertTrue

@RunWith(JUnit4.class)
class TestArtifactsPluginTest {

    @Test
    void whenApplyPlugin_shouldAddTasksAndExtensions() {
        Project project = ProjectBuilder.builder().build()
        project.pluginManager.apply TestArtifactsPlugin

        assertTrue(project.tasks.mergeTestArtifacts instanceof MergeTestArtifacts)
        assertTrue(project.tasks.uploadTestArtifacts instanceof UploadTestArtifacts)
        assertTrue(project.tasks.totals instanceof Totals)
        assertTrue(project.extensions.archiver instanceof TestArtifactsExtension)
    }
}
