package com.brunodles.testartifacts.reportparser

/**
 * A totalizer for a report tool
 */
interface ReportTotalizer {

    /**
     * When grab data from file
     *
     * @param filename the key name for original file report
     * @param data     report content, it may vary depending on data structure
     */
    fun onData(filename: String, data: Map<String, Any>)

    /**
     * Called to collect total or resume from report
     *
     * @return A json structure that will be added as "totals" in report
     */
    fun result(): Map<String, Any>
}