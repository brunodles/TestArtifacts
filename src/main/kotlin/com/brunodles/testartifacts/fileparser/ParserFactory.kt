package com.brunodles.testartifacts.fileparser

object ParserFactory {

    @JvmField
    val XML = XmlParser()

    @JvmField
    val CSV = CsvParser()
}
