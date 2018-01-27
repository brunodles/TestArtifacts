package com.brunodles.testartifacts.reportparser

interface ReportTotalizer {

    /**
     * When grab data from file
     *
     * @param filename the key name for original file report
     * @param data     report content
     */
    fun onData(filename: String, data: Map<String, Any>)

    /**
     * Called to collect total or resume from report
     *
     * @return
     */
    fun result(): Map<String, Any>
}