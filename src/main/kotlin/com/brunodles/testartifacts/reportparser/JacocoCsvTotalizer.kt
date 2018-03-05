package com.brunodles.testartifacts.reportparser

class JacocoCsvTotalizer : ReportTotalizer {

    var covered = 0L
    var missed = 0L

    override fun onData(filename: String, data: Map<String, Any>) {
        data.forEach {
            if (it.value is Map<*, *>) {
                val value = it.value as Map<String, Any?>
                missed += value.getAsLongOr("instructionMissed", 0L)
                covered += value.getAsLongOr("instructionCovered", 0L)
            }
        }
    }

    override fun result(): Map<String, Any> {
        val instructions = (covered + missed)
        return mapOf(
                "covered" to covered,
                "missed" to missed,
                "coveredRate" to Math.round((covered * 100.0) / instructions)
        )
    }
}

@Suppress("ReplaceGetOrSet")
private fun Map<String, Any?>.getAsLongOr(key: String, defaultValue: Long): Long {
    return this.get(key)?.toLongOr(defaultValue) ?: defaultValue
}

private fun Any?.toLongOr(defaultValue: Long): Long {
    return when (this) {
        is String -> this.toLongOrNull() ?: defaultValue
        is Long -> this
        else -> defaultValue
    }
}
