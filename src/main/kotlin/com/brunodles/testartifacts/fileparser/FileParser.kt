package com.brunodles.testartifacts.fileparser

import java.io.File

internal interface FileParser {
    fun parse(file: File): Map<String, Any?>
}
