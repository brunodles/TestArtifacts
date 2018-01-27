package com.brunodles.testartifacts

open class TestArtifactsExtension {

    var files: Map<String, List<String>> = HashMap()
    var projectName: String? = null
    var moduleName: String? = null
    var buildNumber: String = "debug"
    var firebaseUrl: String = "test-artifacts"

    fun firebaseUrl() =
            "https://$firebaseUrl.firebaseio.com/$projectName/$buildNumber/$moduleName.json"

}
