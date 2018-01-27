package com.brunodles.testartifacts.task

import com.brunodles.testartifacts.TestArtifactsExtension
import com.brunodles.testartifacts.TestArtifactsPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

open class UploadTestArtifacts : DefaultTask() {

    @InputFile
    var file = File(project.buildDir, "reports/uploadReports.json")

    @TaskAction
    fun sendToFirebase() {
        val extension = project.extensions.findByName(TestArtifactsPlugin.EXTENSION) as TestArtifactsExtension
        val baseUrl = URL(extension.firebaseUrl())
        val connection = baseUrl.openConnection() as HttpURLConnection
        connection.apply {
            doOutput = true
            requestMethod = "PUT"
            outputStream.bufferedWriter().write(file.readText())
            println("Prepare to send")
            println(content)
            println("Sent to Firebase")
        }
    }
}
