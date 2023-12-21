package com.saadahmedev.tresoro.controller.auth

import com.saadahmedev.tresoro.dto.auth.LoginDto
import com.saadahmedev.tresoro.dto.user.UserDto
import com.saadahmedev.tresoro.entity.user.UserType
import com.saadahmedev.tresoro.service.auth.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/auth")
open class AuthController {

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @PostMapping("create-customer")
    open fun createCustomerAccount(@RequestBody userDto: UserDto?) =
        authenticationService.createAccount(userDto.apply { this?.role = UserType.CUSTOMER })

    @PostMapping("create-admin")
    open fun createAdminAccount(@RequestBody userDto: UserDto?) =
        authenticationService.createAccount(userDto.apply { this?.role = UserType.ADMIN })

    @PostMapping("create-manager")
    @PreAuthorize("hasAuthority('ADMIN')")
    open fun createManagerAccount(@RequestBody userDto: UserDto?) =
        authenticationService.createAccount(userDto.apply { this?.role = UserType.MANAGER })

    @PostMapping("create-hr")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    open fun createHrAccount(@RequestBody userDto: UserDto?) =
        authenticationService.createAccount(userDto.apply { this?.role = UserType.HR })

    @PostMapping("create-employee")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'HR')")
    open fun createEmployeeAccount(@RequestBody userDto: UserDto?) =
        authenticationService.createAccount(userDto.apply { this?.role = UserType.EMPLOYEE })

    @PostMapping("login")
    open fun login(@RequestBody loginDto: LoginDto?) = authenticationService.login(loginDto)
}