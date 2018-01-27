package com.brunodles.kotlin

fun MutableMap<String, Long>.sumChild(key: String, other: Map<out Any?, Any?>) {
    if (!other.containsKey(key))
        return
    if (this.containsKey(key)) {
        val inputValue = this[key]!!
        this.put(key, inputValue + other[key].toString().toLong())
        // other types?
    } else {
        this.put(key, other[key].toString().toLong())
    }
}
