package com.saadahmedev.tresoro.service.auth

import com.saadahmedev.tresoro.dto.auth.LoginDto
import com.saadahmedev.tresoro.dto.user.UserDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
interface AuthenticationService {
    fun createAdminAccount(userDto: UserDto?): ResponseEntity<*>
    fun createManagerAccount(userDto: UserDto?): ResponseEntity<*>
    fun createHrAccount(userDto: UserDto?): ResponseEntity<*>
    fun createEmployeeAccount(userDto: UserDto?): ResponseEntity<*>
    fun createCustomerAccount(userDto: UserDto?): ResponseEntity<*>

    fun login(loginDto: LoginDto?): ResponseEntity<*>
}