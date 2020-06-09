package io.github.multicatch.papajbot.talk

fun keyWordsOf(message: String) = message
        .toLowerCase()
        .replace(Regex("[^\\p{L} ]"), "")
        .split(" ")
        .filter { it.length > 2 }
        .toSet()