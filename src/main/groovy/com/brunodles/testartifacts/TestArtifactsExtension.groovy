package com.brunodles.testartifacts

class TestArtifactsExtension {
    
    public Map<String, List<String>> files = new HashMap<>()
    public String projectName
    public String moduleName
    public String buildNumber
    public String firebaseUrl = 'test-artifacts'

    String firebaseUrl() {
        return "https://${firebaseUrl}.firebaseio.com/${projectName}/${buildNumber}/${moduleName}.json"
    }
}
