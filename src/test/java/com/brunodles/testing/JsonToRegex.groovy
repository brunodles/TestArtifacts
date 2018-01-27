package com.brunodles.testing

/**
 * This class will help to build the JSONs used to assert expected result
 */
class JsonToRegex {

    public static final String REGEX_INVALID_CHARACTERS = "([\\(\\)\\[\\]\\{\\}\\.\\*\\\$\\-\\?])"

    static void main(String[] args) {
        // place the JSON here
        String jsonText = "{'justToCheck':\"\$wowlook-at-this[]()\"}"
        String regex = jsonText.replaceAll(REGEX_INVALID_CHARACTERS, "\\\\\$1")
        println regex
    }
}
