package com.brunodles.testartifacts.task

import com.brunodles.testartifacts.TestArtifactsExtension
import com.brunodles.testartifacts.TestArtifactsPlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

class UploadTestArtifacts extends DefaultTask {

    @InputFile
    File file = new File(project.buildDir, "reports/uploadReports.json")

    @TaskAction
    def sendToFirebase() {
        TestArtifactsExtension extension = project.getExtensions().findByName(TestArtifactsPlugin.EXTENSION) as TestArtifactsExtension
        def baseUrl = new URL(extension.firebaseUrl())
        def connection = baseUrl.openConnection()
        connection.with {
            doOutput = true
            requestMethod = 'PUT'
            outputStream.withWriter { writer ->
                writer << file.text
            }
            content.text
            println "Sent to firebase"
        }
    }
}
