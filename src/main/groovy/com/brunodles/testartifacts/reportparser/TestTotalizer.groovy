package com.brunodles.testartifacts.reportparser

class TestTotalizer implements ReportTotalizer {

    private Map<String, Double> data = [
            errors     : 0D, // All errors, only junit < 4
            tests      : 0D, // All tests completed. error, skipped and failures are included
            failures   : 0D, // assertion erros
            skipped    : 0D, // ignored tests
            successRate: 0D
    ]

    @Override
    void onData(String filename, Map<String, Object> data) {
        this.data.errors += Integer.valueOf(data.errors.toString()) ?: 0
        this.data.failures += Integer.valueOf(data.failures.toString())
        this.data.skipped += Integer.valueOf(data.skipped.toString())
        this.data.tests += Integer.valueOf(data.tests.toString())
    }

    @Override
    Map<String, Object> result() {
        data.success = data.tests - (data.errors + data.failures + data.skipped)
        data.successRate = Math.round(data.success * 10000 / data.tests) / 100
        return data
    }
}
