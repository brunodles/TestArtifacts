package com.brunodles.testartifacts.task

import com.brunodles.testartifacts.TestArtifactsExtension
import com.brunodles.testartifacts.TestArtifactsPlugin
import com.brunodles.testartifacts.reportparser.ReportTotalizer
import com.brunodles.testartifacts.reportparser.ReportType
import groovy.json.JsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import java.text.SimpleDateFormat

import com.brunodles.testartifacts.helpers.StringUtils.removeEspecialCharacters
import org.gradle.api.Project
import java.io.File
import java.util.*

open class MergeTestArtifacts : DefaultTask() {

    @InputFiles
    fun getReports(): List<File> {
        val extension = project.extensions.findByName(TestArtifactsPlugin.EXTENSION) as TestArtifactsExtension
        val files = extension.files
        return files.map { it -> it.value }.flatMap {
            project.fileTree(dir = project.buildDir, include = it).toList()
        }
    }

    @OutputFile
    fun getOutputJson(): File {
        val reportDir = File(project.buildDir, "reports")
        reportDir.mkdirs()
        return File(reportDir, "uploadReports.json")
    }

    @TaskAction
    fun buildReport() {
        val extension = project.extensions.findByName(TestArtifactsPlugin.EXTENSION) as TestArtifactsExtension

        val result: MutableMap<String, Any?> = mergeReports(extension)
        result.put("totals", totals(result))
        result.put("buildInfo", buildInfo(extension))

        writeReport(result)
    }

    private fun mergeReports(extension: TestArtifactsExtension): MutableMap<String, Any?> {
        val files = extension.files
        return files.map { fileData ->
            val type = fileData.key
            val path = fileData.value
            val result: MutableMap<String, Any?> = path.map { project.fileTree(dir = project.buildDir, include = listOf(it)).toList() }
                    .flatten()
                    .filter { it.exists() }
                    .mapNotNull { file ->
                        val reportType = ReportType.valueOf(type)
                        if (file is File)
                            return@mapNotNull removeEspecialCharacters(file.name) to reportType.parse(file)
                        return@mapNotNull "" to null
                    }.toMap().toMutableMap()
            return@map removeEspecialCharacters(type) to result
        }.toMap().toMutableMap()
    }

    private fun totals(json: Map<String, Any?>): Map<String, Any?> {
        val totalizers: Map<String, ReportTotalizer?> = ReportType.values()
                .filter { it.totalizerClass != null }
                .map { it.name to it.newTotalizer() }
                .toMap()
        json.forEach { type, typeFileList ->
            if (totalizers.containsKey(type)
                    && totalizers[type] != null
                    && typeFileList is Map<*, *>)
                typeFileList.forEach { it ->
                    if (it.value is Map<*, *>) {
                        try {
                            val data: Map<String, Any> = it.value as Map<String, Any>
                            val fileName: String = it.key.toString()
                            totalizers[type]!!.onData(fileName, data)
                        } catch (e: Exception) {
                        }
                    }
                }
        }
        return totalizers.mapValues { it.value?.result() ?: emptyMap() }
    }

    private fun buildInfo(extension: TestArtifactsExtension): Map<String, String?> {
        return mapOf("projectName" to extension.projectName,
                "buildNumber" to extension.buildNumber,
                "moduleName" to extension.moduleName,
                "dateTime" to SimpleDateFormat("yyyy.MM.dd HH:MM:ss").format(Date())
        )
    }

    private fun writeReport(result: Map<String, Any?>) {
        val json = JsonBuilder(result).toString()
        val file = getOutputJson()
        println("Saving at '${file.path}'.")
        file.writeText(json)
    }

    private fun Project.fileTree(dir: File?, include: List<String>)
            = project.fileTree(mapOf("dir" to dir, "include" to include))
}

