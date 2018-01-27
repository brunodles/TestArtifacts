package com.brunodles.testartifacts.reportparser

class CheckstyleTotalizer : ReportTotalizer {
    private val data: MutableMap<String, Long> = mutableMapOf("errors" to 0L)

    override fun onData(filename: String, data: Map<String, Any>) {
        var errors: Long = this.data["errors"] ?: 0L
        errors += countCheckstyleErrors(data)
        this.data["errors"] = errors
    }

        override fun result(): Map<String, Any> = data

    companion object {
        private fun countCheckstyleErrors(fileData: Map<String, Any>): Long {
            if (!fileData.containsKey("fileList"))
                return 0L
            var count = 0L
            val fileList = fileData["fileList"]
            if (fileList !is Iterable<*>)
                return 0
            fileList.forEach {
                if (it is Map<*, *> && it.containsKey("errorList")) {
                    val value = it["errorList"]
                    if (value is Map<*, *>)
                        count += value.size
                    else if (value is Iterable<*>)
                        count += value.count()
                }
            }
            return count
        }
    }

}
