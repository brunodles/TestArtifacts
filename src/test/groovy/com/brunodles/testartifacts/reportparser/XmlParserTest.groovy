package com.brunodles.testartifacts.reportparser

import com.brunodles.testing.TestResourceReader
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static org.junit.Assert.assertEquals

@RunWith(JUnit4.class)
class XmlParserTest {

    @Rule
    public TemporaryFolder testProjectDir = new TemporaryFolder()
    private XmlParser xmlParser = new XmlParser()

    @Test
    void whenDoctype_shouldBeIgnored() {
        def result = xmlParser.parse(TestResourceReader.file('ReportParser/doctype_samelinexml'))
        assertEquals([name: "testartifacts"], result)
    }

    @Test
    void whenDoctypeContainsLineBreak_shouldBeIgnored() {
        def result = xmlParser.parse(TestResourceReader.file('ReportParser/doctype_multiline.xml'))
        assertEquals([name: "testartifacts"], result)
    }

}
