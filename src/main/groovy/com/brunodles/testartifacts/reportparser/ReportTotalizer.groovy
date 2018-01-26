package com.brunodles.testartifacts.reportparser

interface ReportTotalizer {

    /**
     * When grab data from file
     * @param filename the key name for original file report
     * @param data report content
     */
    void onData(String filename, Map<String, Object> data)

    /**
     * Called to collect total or resume from report
     * @return
     */
    Map<String, Object> result()
}