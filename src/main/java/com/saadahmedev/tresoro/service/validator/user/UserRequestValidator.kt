package com.saadahmedev.tresoro.service.validator.user

import com.saadahmedev.springboot.dto.ApiResponse
import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.springboot.server.ServerResponse.badRequest
import com.saadahmedev.tresoro.dto.user.UserDto
import com.saadahmedev.tresoro.entity.user.User
import com.saadahmedev.tresoro.repository.user.UserRepository
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.util.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Qualifier(Constants.BeanQualifier.USER_REQUEST_VALIDATOR)
class UserRequestValidator : RequestValidator<User, UserDto, Long, UserRepository>() {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun isCreateRequestValid(body: UserDto?): ResponseEntity<ApiResponse> {
        return when {
            body == null -> badRequest("Create account request body is required")
            body.firstName.isNullOrBlank() -> badRequest(message = "First name is required")
            body.lastName.isNullOrBlank() -> badRequest(message = "Last name is required")
            body.email.isNullOrBlank() -> badRequest(message = "Email is required")
            userRepository.findByEmail(body.email).isPresent -> badRequest("Email already exist")
            body.phone.isNullOrBlank() -> badRequest(message = "Phone is required")
            userRepository.findByPhone(body.phone).isPresent -> badRequest(message = "Phone already exist")
            body.address.isNullOrBlank() -> badRequest(message = "Address is required")
            body.dateOfBrith.isNullOrBlank() -> badRequest(message = "Date of birth is required")
            body.password.isNullOrBlank() -> badRequest(message = "Password is required")
            body.password.length < 6 -> badRequest(message = "Password is to short")
            body.confirmPassword.isNullOrBlank() -> badRequest(message = "Confirm password is required")
            body.password != body.confirmPassword -> badRequest(message = "Password didn't match")

            else -> ServerResponse.ok()
        }
    }
}