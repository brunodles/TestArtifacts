package com.brunodles.testing

final class TestResourceReader {
    private TestResourceReader() {
    }

    static String readResource(String resource) {
        return file(resource).text
    }

    static File file(String resource) {
        def filePath = ClassLoader.systemClassLoader.getResource(resource).file
        def file = new File(filePath)
        file
    }
}
