package com.brunodles.testartifacts.reportparser

import static com.brunodles.testartifacts.helpers.XmlUtils.nested

class XmlParser implements FileParser {
    def parser = new groovy.util.XmlParser(false, false, false)

    @Override
    def parse(File file) {
        String text = file.text.replaceAll("\\<\\!?DOCTYPE.*?\\>", "")
        return nested(parser.parseText(text))
    }
}
