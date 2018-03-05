package com.brunodles.testartifacts.fileparser

import java.io.File

/**
 * A class that parse a file to a Json Format
 */
internal interface FileParser {

    /**
     * Parse the file into a nested map, like a json file
     */
    fun parse(file: File): Map<String, Any?>
}
