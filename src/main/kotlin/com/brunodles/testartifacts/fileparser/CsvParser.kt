package com.brunodles.testartifacts.fileparser

import com.brunodles.kotlin.splitEachLine
import com.brunodles.testartifacts.helpers.StringUtils.removeEspecialCharacters
import com.brunodles.testartifacts.helpers.StringUtils.underscoreToCamelCase
import java.io.File
import java.util.*

class CsvParser : FileParser {
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
            val key = "${fields[0]}_${fields[1]}_${fields[2]}"
            jsonRoot.put(removeEspecialCharacters(key), values)
        }
        return jsonRoot
    }

}