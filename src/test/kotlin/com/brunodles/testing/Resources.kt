package com.brunodles.testing

import java.io.File

object Resources {
    fun readResource(resource: String): String {
        return file(resource).readText()
    }

    fun file(resource: String): File {
        val filePath = ClassLoader.getSystemClassLoader().getResource(resource).file
        return File(filePath)
    }
}
