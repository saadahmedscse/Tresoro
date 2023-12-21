package com.saadahmedev.tresoro.service.validator

data class ResponseWithResult<T, R>(
    val response: T,
    val result: R?
)