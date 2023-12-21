package com.saadahmedev.tresoro.service.auth

import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.tresoro.dto.auth.LoginDto
import com.saadahmedev.tresoro.dto.user.UserDto
import com.saadahmedev.tresoro.repository.user.UserRepository
import com.saadahmedev.tresoro.security.JwtService
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.util.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl : AuthenticationService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    @Qualifier(Constants.BeanQualifier.SIGN_UP_REQUEST_VALIDATOR)
    private lateinit var signUpValidator: RequestValidator

    @Autowired
    @Qualifier(Constants.BeanQualifier.LOGIN_REQUEST_VALIDATOR)
    private lateinit var loginValidator: RequestValidator

    override fun createAccount(userDto: UserDto?): ResponseEntity<*> {
        val validationResult = signUpValidator.isValid(userDto)
        if (validationResult.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return validationResult

        return try {
            userRepository.save(requireNotNull(userDto).toEntity(passwordEncoder))
            ServerResponse.created(message = "Account created successfully")
        } catch (e: Exception) {
            ServerResponse.internalServerError(exception = e)
        }
    }

    override fun login(loginDto: LoginDto?): ResponseEntity<*> {
        val validationResult = loginValidator.isValid(loginDto)
        if (validationResult.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return validationResult

        return if (authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginDto?.email, loginDto?.password)).isAuthenticated) {
            ServerResponse.login(
                message = "Logged in successfully",
                accessToken = jwtService.generateAccessToken(requireNotNull(loginDto?.email))
            )
        } else ServerResponse.unauthorized(message = "Username or password not valid")
    }
}