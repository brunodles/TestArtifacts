package com.brunodles.testing

import org.junit.Assert

import java.util.regex.Pattern

object Assertions {

    fun assertMatches(pattern: String, result: String) {
        assertMatches(Pattern.compile(pattern), result)
    }

    fun assertMatches(pattern: Pattern, result: String) {
        val printableExpected = pattern.toString().replace("\\\\([\\{\\}\\[\\]\\(\\)\\-\\:\\.\\/\\\\\\$])".toRegex(), "$1")
//        val tempResult = result.replace("\"dateTime\":\".*?\"".toRegex(), "\"dateTime\":\"\\.\\*\\?\"")
//        Assert.assertEquals(printableExpected, tempResult)
        Assert.assertTrue("Failed to match regex\n" +
                "Expected: $printableExpected\n" +
                "     got: $result",
                pattern.matcher(result).matches())
    }
}
