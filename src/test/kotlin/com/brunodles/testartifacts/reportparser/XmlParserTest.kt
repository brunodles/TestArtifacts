package com.brunodles.testartifacts.reportparser

import com.brunodles.testartifacts.fileparser.XmlParser
import com.brunodles.testing.Resources
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class XmlParserTest {

    @Rule
    @JvmField
    val testProjectDir = TemporaryFolder()

    private val xmlParser = XmlParser()
    private val expected = mapOf("name" to "testartifacts")

    @Test
    fun whenDoctype_shouldBeIgnored() {
        val result = xmlParser.parse(Resources.file("ReportParser/doctype_samelinexml"))
        assertEquals(expected, result)
    }

    @Test
    fun whenDoctypeContainsLineBreak_shouldBeIgnored() {
        val result = xmlParser.parse(Resources.file("ReportParser/doctype_multiline.xml"))
        assertEquals(expected, result)
    }

}
