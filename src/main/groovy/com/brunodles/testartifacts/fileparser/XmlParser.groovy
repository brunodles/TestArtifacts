package com.brunodles.testartifacts.fileparser

import static com.brunodles.testartifacts.helpers.StringUtils.removeEspecialCharacters

class XmlParser implements FileParser {
    def parser = new groovy.util.XmlParser(false, false, false)

    @Override
    def parse(File file) {
        String text = file.text.replaceAll("(?smi)<!DOCTYPE.*?>", "")
        return nested(parser.parseText(text))
    }

    static def nested(Node node) {
        return nested(node) { "${removeEspecialCharacters(it.name().toString())}List".toString() }
    }

    static def nested(Node node, KeyNameBuilder nameBuilder) {
        def result = node.attributes()
        def children = new LinkedHashMap<String, List<Map>>()
        node.children().each { child ->
            if (child instanceof Node) {
                String name = nameBuilder.nameFor(child)
                List list = children.get(name)
                if (list == null)
                    list = new LinkedList()
                list.add(nested(child, nameBuilder))
                children.put(name, list)
            } else {
                result = child
            }
        }
        if (!children.isEmpty())
            result.putAll(children)
        return result
    }

    interface KeyNameBuilder {
        String nameFor(Node child)
    }

}
