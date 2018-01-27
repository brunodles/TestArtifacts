package com.brunodles.testartifacts.reportparser

import com.brunodles.kotlin.sumChild

class TestTotalizer : ReportTotalizer {

    private val data: MutableMap<String, Long> = mutableMapOf(
            "errors" to 0L, // All errors, only junit < 4
            "tests" to 0L, // All tests completed. error, skipped and failures are included
            "failures" to 0L, // assertion erros
            "skipped" to 0L, // ignored tests
            "successRate" to 0L
    )

    override fun onData(filename: String, data: Map<String, Any>) {
        this.data.sumChild("errors", data)
        this.data.sumChild("failures", data)
        this.data.sumChild("skipped", data)
        this.data.sumChild("tests", data)
    }

    override fun result(): Map<String, Any> {
        data["success"] = data["tests"]!! - (data["errors"]!! + data["failures"]!! + data["skipped"]!!)
        data["successRate"] = Math.round(data["success"]!! * 100.0 / data["tests"]!!)
        return data
    }
}
