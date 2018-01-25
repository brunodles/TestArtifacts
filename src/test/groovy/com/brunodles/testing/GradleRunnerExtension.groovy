package com.brunodles.testing

import org.gradle.testkit.runner.GradleRunner

class GradleRunnerExtension {

    static GradleRunner withJacoco(GradleRunner runner) {
        def resource = Resources.readResource('testkit-gradle.properties')
        def file = new File(runner.projectDir, "gradle.properties")
        file.createNewFile()
        file << resource
        return runner
    }
}
