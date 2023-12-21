package com.saadahmedev.tresoro.util

import java.util.Random

fun String?.ifNullOrBlank(default: () -> String): String {
    return if (this.isNullOrBlank()) default.invoke() else this
}

fun getUniqueAccountNumber(length: Int = 17): String {
    return (1..length).map {
        Random().nextInt(0, 10)
    }.joinToString("")
}