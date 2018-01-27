package com.brunodles.testing

import org.gradle.testkit.runner.GradleRunner
import java.io.File

fun GradleRunner.withJacoco(): GradleRunner {
    val resource = Resources.readResource("testkit-gradle.properties")
    val file = File(this.projectDir, "gradle.properties")
    file.createNewFile()
    file.writeText(resource)
    return this
}
