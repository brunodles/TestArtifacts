package com.brunodles.testartifacts

class TestArtifactsExtension {
    
    Map<String, List<String>> files
    String projectName
    String moduleName
    String buildNumber
    String firebaseUrl = 'test-artifacts'

    String firebaseUrl() {
        return "https://${firebaseUrl}.firebaseio.com/${projectName}/${buildNumber}/${moduleName}.json"
    }
}
