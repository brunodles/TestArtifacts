package com.brunodles.testartifacts.reportparser

class CheckstyleTotalizer implements ReportTotalizer {

    private Map<String, Double> data = [errors: 0D]

    @Override
    void onData(String filename, Map<String, Object> data) {
        data.errors += countCheckstyleErrors(data)
    }

    @Override
    Map<String, Object> result() {
        return data
    }

    private static int countCheckstyleErrors(Map fileData) {
        if (!fileData.containsKey("fileList"))
            return 0
        int count = 0
        fileData.get('fileList').each {
            it.each { k, v ->
                if ("errorList" == k)
                    count += v.size
            }
        }
        return count
    }
}
