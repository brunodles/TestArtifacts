package com.brunodles.testartifacts.helpers

import static com.brunodles.testartifacts.helpers.StringUtils.removeEspecialCharacters

final class XmlUtils {

    private XmlUtils() {
    }

    static def nested(Node node) {
        def result = node.attributes()
        def children = new LinkedHashMap<String, List<Map>>()
        node.children().each { child ->
            if (child instanceof Node) {
                String name = removeEspecialCharacters(child.name().toString())
                String childKeyName = "${name}List"
                List list = children.get(childKeyName)
                if (list == null)
                    list = new LinkedList()
                list.add(nested(child))
                children.put(childKeyName, list)
            } else {
                result = child
            }
        }
        if (!children.isEmpty())
            result.putAll(children)
        return result
    }
}
