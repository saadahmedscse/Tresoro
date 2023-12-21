package com.saadahmedev.tresoro.service.validator

import com.saadahmedev.springboot.dto.ApiResponse
import com.saadahmedev.springboot.server.ServerResponse
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
interface RequestValidator {

    fun isValid(dto: Any?): ResponseEntity<ApiResponse>
}