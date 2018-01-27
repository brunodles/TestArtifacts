package com.brunodles.testartifacts.helpers

import java.text.Normalizer

object StringUtils {

    @JvmStatic
    fun removeEspecialCharacters(string: String?): String {
        return Normalizer.normalize(string, Normalizer.Form.NFD)
                .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
                .replace("[^\\d\\w]+".toRegex(), "")
    }

    @JvmStatic
    fun underscoreToCamelCase(underscore: String?): String {
        return if (underscore.isNullOrBlank())
            ""
        else
            underscore!!.replace("[_ ](\\w)".toRegex()) { it.groups[1]?.value?.toUpperCase() ?: "" }
    }
}
