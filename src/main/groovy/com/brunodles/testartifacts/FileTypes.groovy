package com.brunodles.testartifacts

enum FileTypes {
    checkstyle(XML),
    jacoco(XML),
    jacocoCsv(CSV),
    lint(XML),
    test(CSV)

    private static String XML = "xml"
    private static String CSV = "CSV"

    final String decoder

    FileTypes(String decoder) {
        this.decoder = decoder
    }
}
