package com.brunodles.testartifacts.fileparser

import com.brunodles.testartifacts.helpers.StringUtils.removeEspecialCharacters
import groovy.util.Node
import groovy.util.XmlParser
import java.io.File
import java.util.*

class XmlParser : FileParser {
    val parser = XmlParser(false, false, false)

    override fun parse(file: File): Map<String, Any?> {
        val text = file.readText().replace("(?smi)<!DOCTYPE.*?>".toRegex(), "")
        return nested(parser.parseText(text))
    }

    companion object {
        fun nested(node: Node): Map<String, Any?> {
            return nested(node) { "${removeEspecialCharacters(it.name().toString())}List" }
        }

        fun nested(node: Node, nameBuilder: (Node) -> String): Map<String, Any?> {
            var result: MutableMap<String, Any?> = node.attributes().mapKeys { it.key.toString() }.toMutableMap()
            val children = LinkedHashMap<String, MutableList<Map<String, Any?>>>()
            node.children().forEach { child ->
                if (child is Node) {
                    val name = nameBuilder.invoke(child)
                    val list = childWithName(children, name)
                    list.add(nested(child, nameBuilder))
                    children.put(name, list)
                } else if (child is Map<*, *>) {
                    result = child.mapKeys { it.key.toString() }.toMutableMap()
                }
            }
            if (!children.isEmpty())
                result.putAll(children)
            return result
        }

        private fun childWithName(children: LinkedHashMap<String, MutableList<Map<String, Any?>>>, name: String) =
                if (children.containsKey(name)) children[name] ?: LinkedList() else LinkedList()

    }
}