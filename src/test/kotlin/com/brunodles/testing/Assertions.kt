package com.brunodles.testing

import org.junit.Assert

import java.util.regex.Pattern

object Assertions {

    fun assertMatches(pattern: String, result: String) {
        assertMatches(Pattern.compile(pattern), result)
    }

    fun assertMatches(pattern: Pattern, result: String) {
        val expected = pattern.toString().replace("\\\\([\\{\\}\\[\\]\\(\\)\\-\\:\\.\\/\\\\])", "\\$1")
        Assert.assertTrue("Failed to match regex\n" +
                "Expected: $expected\n" +
                "     got: $result",
                pattern.matcher(result).matches())
    }
}
