package com.brunodles.testartifacts

import com.brunodles.auto.gradleplugin.AutoPlugin
import com.brunodles.testartifacts.task.MergeTestArtifacts
import com.brunodles.testartifacts.task.UploadTestArtifacts
import org.gradle.api.Plugin
import org.gradle.api.Project

@AutoPlugin("testartifacts")
class TestArtifactsPlugin : Plugin<Project> {

    companion object {
        @JvmField
        val EXTENSION: String = "archiver"
    }

    override fun apply(project: Project) {
        project.extensions.create(EXTENSION, TestArtifactsExtension::class.java)
        project.task("mergeTestArtifacts",
                "type" to MergeTestArtifacts::class.java,
                "group" to EXTENSION)
        project.task("uploadTestArtifacts",
                "type" to UploadTestArtifacts::class.java,
                "group" to EXTENSION)
//        project.task('mergeTestArtifacts', type: MergeTestArtifacts, group: EXTENSION)
//        project.task('uploadTestArtifacts', type: UploadTestArtifacts, group: EXTENSION)
    }

    private fun Project.task(name: String, vararg pairs: Pair<String, Any?>) {
        this.task(pairs.toMap(), name)
    }
}