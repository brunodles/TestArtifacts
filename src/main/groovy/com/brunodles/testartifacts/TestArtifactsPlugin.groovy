package com.brunodles.testartifacts

import com.brunodles.auto.gradleplugin.AutoPlugin
import com.brunodles.testartifacts.task.MergeTestArtifacts
import com.brunodles.testartifacts.task.Totals
import com.brunodles.testartifacts.task.UploadTestArtifacts
import org.gradle.api.Plugin
import org.gradle.api.Project

@AutoPlugin("testartifacts")
class TestArtifactsPlugin implements Plugin<Project> {

    public static final String EXTENSION = "archiver"

    @Override
    void apply(Project project) {
        project.extensions.create(EXTENSION, TestArtifactsExtension)
        project.task('mergeTestArtifacts', type: MergeTestArtifacts)
        project.task('UploadTestArtifacts', type: UploadTestArtifacts)
        project.task('totals', type: Totals)
    }
}
