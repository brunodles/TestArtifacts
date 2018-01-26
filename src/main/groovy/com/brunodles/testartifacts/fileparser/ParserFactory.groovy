package com.brunodles.testartifacts.fileparser

final class ParserFactory {

    static final FileParser XML = new XmlParser()
    static final FileParser CSV = new CsvParser()

    private ParserFactory() {
    }
}
