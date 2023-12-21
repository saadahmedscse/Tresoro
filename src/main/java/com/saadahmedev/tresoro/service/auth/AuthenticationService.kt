package com.saadahmedev.tresoro.service.auth

import com.saadahmedev.tresoro.dto.auth.LoginDto
import com.saadahmedev.tresoro.dto.user.UserDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
interface AuthenticationService {
    fun createAccount(userDto: UserDto?): ResponseEntity<*>

    fun login(loginDto: LoginDto?): ResponseEntity<*>
}