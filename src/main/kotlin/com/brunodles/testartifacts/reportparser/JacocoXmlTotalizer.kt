package com.brunodles.testartifacts.reportparser

import com.brunodles.kotlin.sumChild

class JacocoXmlTotalizer : ReportTotalizer {
    private val data: MutableMap<String, Long> = mutableMapOf(
            "covered" to 0L,
            "missed" to 0L,
            "coveredRate" to 0L
    )

    override fun onData(filename: String, data: Map<String, Any>) {
        if (!data.containsKey("counterList"))
            return
        val counterList = data["counterList"]
        if (counterList is Collection<*>)
            counterList.forEach { counterData ->
                if (counterData is Map<*, *>
                        && counterData.containsKey("type")
                        && "INSTRUCTION" == counterData["type"]) {
                    this.data.sumChild("missed", counterData)
//                    this.data["missed"] = this.data["missed"]!! + counterData["missed"].toString().toDouble()
                    this.data.sumChild("covered", counterData)
//                    this.data["covered"] = this.data["covered"]!! + counterData["covered"].toString().toDouble()
                }
            }
    }

    override fun result(): Map<String, Any> {
        val covered: Long = data["covered"] ?: 0L
        val missed: Long = data["missed"] ?: 0L
        val instructions = (covered + missed)
        if (instructions > 0)
            data["coveredRate"] = Math.round((covered * 100.0) / instructions)
        return data
    }
}
