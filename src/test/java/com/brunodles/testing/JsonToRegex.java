package com.brunodles.testing;

/**
 * This class will help to build the JSONs used to assert expected result
 */
class JsonToRegex {

    /**
     * Simple regex to remove regex especial characters that's also used on json
     */
    private static final String REGEX_INVALID_CHARACTERS = "([\\(\\)\\[\\]\\{\\}\\.\\*\\$\\-\\?])";

    public static void main(String[] args) {
        // replace the JSON
        String jsonText = "{\"checkstyle\":{},\"jacoco\":{},\"jacocoCsv\":{},\"lint\":{},\"test\":{},\"totals\":{\"checkstyle\":{\"errors\":0},\"jacoco\":{\"covered\":0,\"missed\":0,\"coveredRate\":0},\"test\":{\"errors\":0,\"tests\":0,\"failures\":0,\"skipped\":0,\"successRate\":0,\"success\":0}},\"buildInfo\":{\"projectName\":\"AnimeWatcher\",\"buildNumber\":\"123\",\"moduleName \":\"Explorer\",\"dateTime\":\"2018.01.27 11:01:05\"}}";
        String regex = jsonText.replaceAll(REGEX_INVALID_CHARACTERS, "\\\\$1");
        regex = regex.replaceAll("\"dateTime\":\".*?\"", "\"dateTime\":\"\\.\\*\\?\"");
        System.out.println(regex);
    }
}
