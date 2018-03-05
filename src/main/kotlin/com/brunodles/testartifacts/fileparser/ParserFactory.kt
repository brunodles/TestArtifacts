package com.brunodles.testartifacts.fileparser

object ParserFactory {

    /**
     * A xml Parser
     */
    @JvmField
    val XML = XmlParser()

    /**
     * A CSV parser that uses the first 3 values as key.
     */
    @JvmField
    val CSV = CsvParser { fields -> "${fields[0]}_${fields[1]}_${fields[2]}" }
}
