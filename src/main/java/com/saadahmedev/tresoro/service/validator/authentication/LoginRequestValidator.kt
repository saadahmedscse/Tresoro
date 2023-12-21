package com.saadahmedev.tresoro.service.validator.authentication

import com.saadahmedev.springboot.dto.ApiResponse
import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.springboot.server.ServerResponse.badRequest
import com.saadahmedev.tresoro.dto.auth.LoginDto
import com.saadahmedev.tresoro.repository.user.UserRepository
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.util.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Qualifier(Constants.BeanQualifier.LOGIN_REQUEST_VALIDATOR)
class LoginRequestValidator : RequestValidator {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun isValid(dto: Any?): ResponseEntity<ApiResponse> {
        val loginDto = dto as LoginDto?

        return when {
            loginDto == null -> badRequest(message = "Login request body is required")
            loginDto.email.isNullOrBlank() -> badRequest(message = "Email is required")
            userRepository.findByEmail(loginDto.email).isEmpty -> badRequest("Email does not exist")
            loginDto.password.isNullOrBlank() -> badRequest(message = "Password is required")

            else -> ServerResponse.ok()
        }
    }
}