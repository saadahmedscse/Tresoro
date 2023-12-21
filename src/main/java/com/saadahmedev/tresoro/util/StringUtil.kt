package com.saadahmedev.tresoro.util

fun String?.ifNullOrBlank(default: () -> String): String {
    return if (this.isNullOrBlank()) default.invoke() else this
}