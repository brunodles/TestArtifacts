package com.brunodles.testartifacts.reportparser

import com.brunodles.testartifacts.fileparser.FileParser
import com.brunodles.testartifacts.fileparser.ParserFactory
import com.brunodles.testartifacts.fileparser.ParserFactory.CSV
import com.brunodles.testartifacts.fileparser.ParserFactory.XML
import java.io.File

enum class ReportType constructor(private val parser: FileParser, val totalizerClass: Class<out ReportTotalizer>?) : FileParser {
    checkstyle(ParserFactory.XML, CheckstyleTotalizer::class.java),
    jacoco(XML, JacocoXmlTotalizer::class.java),
    jacocoCsv(CSV, null),
    lint(XML, null),
    test(XML, TestTotalizer::class.java);

    override fun parse(file: File): Map<String, Any?> {
        return parser.parse(file)
    }

    /**
     * Beware this may return a null object
     *
     * @return a new totalizer
     */
    fun newTotalizer(): ReportTotalizer? {
        if (totalizerClass != null) {
            try {
                return totalizerClass.newInstance()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }
        return null
    }
}
