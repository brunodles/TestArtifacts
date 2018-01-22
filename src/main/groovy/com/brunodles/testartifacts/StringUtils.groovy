package com.brunodles.testartifacts

class StringUtils {

    static def nested(Node node) {
        def result = node.attributes()
        def children = new LinkedHashMap<String, List<Map>>()
        node.children().each { child ->
            if (child instanceof Node) {
                String name = fixKeyIfNeeded(child.name().toString())
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

    static String fixKeyIfNeeded(String url) {
        return url.replaceAll("[^\\d\\w]+", "")
    }

    static String underscoreToCamelCase(String underscore) {
        if (!underscore || underscore.isAllWhitespace()) {
            return ''
        }
        return underscore.replaceAll(/_\w/) { it[1].toUpperCase() }
    }
}
