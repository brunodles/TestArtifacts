package com.brunodles.testing

final class Resources {
    private Resources() {
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
