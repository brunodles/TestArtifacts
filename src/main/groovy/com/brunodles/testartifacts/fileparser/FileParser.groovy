package com.brunodles.testartifacts.fileparser

interface FileParser {
    Map<String, Object> parse(File file)
}
