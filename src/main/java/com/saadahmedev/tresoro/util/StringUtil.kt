package com.saadahmedev.tresoro.util

import java.util.Random

private const val CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz"

fun String?.ifNullOrBlank(default: () -> String): String {
    return if (this.isNullOrBlank()) default.invoke() else this
}

fun getUniqueAccountNumber(length: Int = 17): String {
    return (1..length).map {
        Random().nextInt(0, 10)
    }.joinToString("")
}

fun getTransactionNumber(length: Int = 10): String {
    val stringBuilder = StringBuilder()
    val random = Random()

    for (i in 0 until length) {
        stringBuilder.append(CHARACTERS[random.nextInt(0, CHARACTERS.length)])
    }

    return stringBuilder.toString()
}

fun String.isDouble(): Boolean {
    return try {
        this.toDouble()
        true
    } catch (e: Exception) {
        false
    }
}