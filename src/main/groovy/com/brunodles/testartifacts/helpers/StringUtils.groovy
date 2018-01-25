package com.brunodles.testartifacts.helpers

import java.text.Normalizer

final class StringUtils {

    private StringUtils() {
    }

    static String removeEspecialCharacters(String string) {
        return Normalizer.normalize(string, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .replaceAll("[^\\d\\w]+", "")
    }

    static String underscoreToCamelCase(String underscore) {
        if (!underscore || underscore.isAllWhitespace()) {
            return ''
        }
        return underscore.replaceAll(/[_ ]\w/) { it[1].toUpperCase() }
    }
}
