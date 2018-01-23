package com.brunodles.testing

import org.junit.Assert

import java.util.regex.Pattern

final class Assertions {
    private Assertions() {
    }

    static void assertMatches(Pattern pattern, String result) {
        Assert.assertTrue("Expected: ${pattern.toString()}\n" +
                "     got: ${result}", pattern.matcher(result).matches())
    }
}
