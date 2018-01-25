package com.brunodles.testartifacts.reportparser

import static ParserFactory.*

enum ReportType implements FileParser {
    checkstyle(XML),
    jacoco(XML),
    jacocoCsv(CSV),
    lint(XML),
    test(XML)

    final FileParser parser

    ReportType(FileParser parser) {
        this.parser = parser
    }

    @Override
    def parse(File file) {
        return parser.parse(file)
    }
}
