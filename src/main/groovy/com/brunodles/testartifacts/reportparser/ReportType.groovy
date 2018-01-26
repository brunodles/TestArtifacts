package com.brunodles.testartifacts.reportparser

import com.brunodles.testartifacts.fileparser.FileParser

import static com.brunodles.testartifacts.fileparser.ParserFactory.*

enum ReportType implements FileParser {
    checkstyle(XML, CheckstyleTotalizer),
    jacoco(XML, JacocoXmlTotalizer),
    jacocoCsv(CSV, null),
    lint(XML, null),
    test(XML, TestTotalizer)

    final FileParser parser
    final Class<ReportTotalizer> totalizerClass

    ReportType(FileParser parser, Class<ReportTotalizer> totalizerClass) {
        this.parser = parser
        this.totalizerClass = totalizerClass
    }

    @Override
    def parse(File file) {
        return parser.parse(file)
    }

    /**
     * Beware this may return a null object
     * @return a new totalizer
     */
    ReportTotalizer newTotalizer() {
        if (totalizerClass)
            return totalizerClass.newInstance()
        return null
    }
}
