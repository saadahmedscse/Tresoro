package com.saadahmedev.tresoro.util

import java.util.Date

object DateUtil {

    fun timeInstant() = Date().toInstant().toString()
}