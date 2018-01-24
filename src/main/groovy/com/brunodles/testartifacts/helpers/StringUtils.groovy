package com.brunodles.testartifacts.helpers

final class StringUtils {

    private StringUtils() {
    }

    static String removeEspecialCharacters(String url) {
        return url.replaceAll("[^\\d\\w]+", "")
    }

    static String underscoreToCamelCase(String underscore) {
        if (!underscore || underscore.isAllWhitespace()) {
            return ''
        }
        return underscore.replaceAll(/_\w/) { it[1].toUpperCase() }
    }
}
