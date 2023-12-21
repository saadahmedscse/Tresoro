package com.saadahmedev.tresoro.controller.auth

import com.saadahmedev.tresoro.dto.auth.LoginDto
import com.saadahmedev.tresoro.dto.user.UserDto
import com.saadahmedev.tresoro.entity.user.UserType
import com.saadahmedev.tresoro.service.auth.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/auth")
class AuthController {

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @PostMapping("create-customer")
    fun createCustomerAccount(@RequestBody userDto: UserDto?): ResponseEntity<*> = authenticationService.createCustomerAccount(userDto.apply { this?.role = UserType.CUSTOMER })

    @PostMapping("login")
    fun login(@RequestBody loginDto: LoginDto?) = authenticationService.login(loginDto)
}