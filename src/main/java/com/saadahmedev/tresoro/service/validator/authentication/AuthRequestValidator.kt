package com.saadahmedev.tresoro.service.validator.authentication

import com.saadahmedev.springboot.dto.ApiResponse
import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.tresoro.dto.auth.LoginDto
import com.saadahmedev.tresoro.entity.user.User
import com.saadahmedev.tresoro.repository.user.UserRepository
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.util.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Qualifier(Constants.BeanQualifier.AUTH_REQUEST_VALIDATOR)
class AuthRequestValidator : RequestValidator<User, LoginDto, Long, UserRepository>() {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun isCreateRequestValid(body: LoginDto?): ResponseEntity<ApiResponse> {
        return when {
            body == null -> ServerResponse.badRequest(message = "Login request body is required")
            body.email.isNullOrBlank() -> ServerResponse.badRequest(message = "Email is required")
            userRepository.findByEmail(body.email).isEmpty -> ServerResponse.badRequest("Email does not exist")
            body.password.isNullOrBlank() -> ServerResponse.badRequest(message = "Password is required")

            else -> ServerResponse.ok()
        }
    }
}