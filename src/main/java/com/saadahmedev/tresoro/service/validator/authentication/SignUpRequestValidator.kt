package com.saadahmedev.tresoro.service.validator.authentication

import com.saadahmedev.springboot.dto.ApiResponse
import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.springboot.server.ServerResponse.badRequest
import com.saadahmedev.tresoro.dto.user.UserDto
import com.saadahmedev.tresoro.repository.user.UserRepository
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.util.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Qualifier(Constants.BeanQualifier.SIGN_UP_REQUEST_VALIDATOR)
class SignUpRequestValidator : RequestValidator {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun isValid(dto: Any?): ResponseEntity<ApiResponse> {
        val user = dto as UserDto?

        return when {
            user == null -> badRequest("Create account request body is required")
            user.firstName.isNullOrBlank() -> badRequest(message = "First name is required")
            user.lastName.isNullOrBlank() -> badRequest(message = "Last name is required")
            user.email.isNullOrBlank() -> badRequest(message = "Email is required")
            userRepository.findByEmail(user.email).isPresent -> badRequest("Email already exist")
            user.phone.isNullOrBlank() -> badRequest(message = "Phone is required")
            userRepository.findByPhone(user.phone).isPresent -> badRequest(message = "Phone already exist")
            user.address.isNullOrBlank() -> badRequest(message = "Address is required")
            user.dateOfBrith.isNullOrBlank() -> badRequest(message = "Date of birth is required")
            user.password.isNullOrBlank() -> badRequest(message = "Password is required")
            user.password.length < 6 -> badRequest(message = "Password is to short")
            user.confirmPassword.isNullOrBlank() -> badRequest(message = "Confirm password is required")
            user.password != user.confirmPassword -> badRequest(message = "Password didn't match")

            else -> ServerResponse.ok()
        }
    }
}