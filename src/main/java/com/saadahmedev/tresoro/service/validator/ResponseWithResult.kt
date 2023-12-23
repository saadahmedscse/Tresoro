package com.saadahmedev.tresoro.service.validator

data class ResponseWithResult<T, R>(
    val var1: T,
    val var2: R?
)