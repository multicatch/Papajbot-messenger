package io.github.multicatch.papajbot.talk

fun keyWordsOf(message: String, minLength: Int = 2) = message
        .toLowerCase()
        .replace(Regex("[^\\p{L} ]"), "")
        .split(" ")
        .filter { it.length > minLength }
        .toSet()