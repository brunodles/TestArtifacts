package com.brunodles.testartifacts.reportparser

import static com.brunodles.testartifacts.helpers.StringUtils.removeEspecialCharacters
import static com.brunodles.testartifacts.helpers.StringUtils.underscoreToCamelCase

final class CsvParser implements FileParser {
    @Override
    def parse(File file) {
        boolean header = true
        def fieldNames = []
        def jsonRoot = new HashMap()
        file.splitEachLine(",") { fields ->
            if (header) {
                fieldNames = fields.collect { underscoreToCamelCase(it.toLowerCase()) }
                header = false
                return
            }
            def values = new HashMap()
            for (int i = 0; i < fieldNames.size(); i++)
                values.put(fieldNames.get(i), fields.get(i))
            def key = "${fields.get(0)}_${fields.get(1)}_${fields.get(2)}"
            //noinspection GroovyVariableNotAssigned
            jsonRoot.put(removeEspecialCharacters(key), values)
            return
        }

        return jsonRoot
    }
}
