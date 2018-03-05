package com.brunodles.testartifacts.fileparser

import com.brunodles.kotlin.splitEachLine
import com.brunodles.testartifacts.helpers.StringUtils.removeEspecialCharacters
import com.brunodles.testartifacts.helpers.StringUtils.underscoreToCamelCase
import java.io.File
import java.util.*

/**
 * Parse CSV file into a Json
 * It will use the 3 firsts values as an item key.
 * Inside that item it will use the headers name as key and read each line to fill it.
 */
class CsvParser(val keyBuilder: (fields: List<String>) -> String) : FileParser {

    /**
     * Parse Csv File to Json
     */
    override fun parse(file: File): Map<String, Any?> {
        var header = true
        var fieldNames: List<String> = emptyList()
        val jsonRoot = HashMap<String, Any>()
        file.splitEachLine(",") { fields ->
            if (header) {
                fieldNames = fields.map { underscoreToCamelCase(it.toLowerCase()) }
                header = false
                return@splitEachLine
            }
            val values = HashMap<String, Any>()
            fieldNames.forEachIndexed { index, fieldName ->
                values.put(fieldName, fields[index])
            }
            val key = keyBuilder(fields)
            jsonRoot.put(removeEspecialCharacters(key), values)
        }
        return jsonRoot
    }

}