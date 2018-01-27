package com.brunodles.kotlin

import java.io.File

fun File.splitEachLine(s: String, func: (List<String>) -> Unit)
        = this.readLines().forEach { line -> func.invoke(line.split(s)) }
