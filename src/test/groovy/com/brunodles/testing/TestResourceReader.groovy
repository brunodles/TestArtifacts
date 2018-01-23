package com.brunodles.testing

final class TestResourceReader {
    private TestResourceReader() {
    }

    static String readResource(String resource) {
        def filePath = ClassLoader.systemClassLoader.getResource(resource).file
        def file = new File(filePath)
        return file.text
    }
}
