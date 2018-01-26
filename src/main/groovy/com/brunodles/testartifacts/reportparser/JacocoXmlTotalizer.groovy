package com.brunodles.testartifacts.reportparser

class JacocoXmlTotalizer implements ReportTotalizer {

    private Map<String, Double> data = [
            covered    : 0D,
            missed     : 0D,
            coveredRate: 0D
    ]

    @Override
    void onData(String filename, Map<String, Object> fileData) {
        fileData.get('counterList').each { counterData ->
            if ("INSTRUCTION" == counterData.type) {
                this.data.missed += Integer.valueOf(counterData.missed.toString())
                this.data.covered += Integer.valueOf(counterData.covered.toString())
            }
        }
    }

    @Override
    Map<String, Object> result() {
        def instructions = (data.covered + data.missed)
        if (instructions > 0)
            data.coveredRate = Math.round(data.covered * 10000 / instructions) / 100
        return data
    }
}
