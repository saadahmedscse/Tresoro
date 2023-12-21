package com.saadahmedev.tresoro.dto.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.saadahmedev.tresoro.entity.user.User
import com.saadahmedev.tresoro.entity.user.UserType
import com.saadahmedev.tresoro.util.DateUtil
import org.springframework.security.crypto.password.PasswordEncoder

data class UserDto(
    @JsonProperty("first_name")
    val firstName: String? = null,
    @JsonProperty("last_name")
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    var role: UserType? = null,
    val address: String? = null,
    @JsonProperty("date_of_birth")
    val dateOfBrith: String? = null,
    val password: String? = null,
    @JsonProperty("confirm_password")
    val confirmPassword: String? = null
) {

    fun toEntity(passwordEncoder: PasswordEncoder): User {
        return User(
            firstName = firstName,
            lastName = lastName,
            fullName = "$firstName $lastName",
            email = email,
            phone = phone,
            role = role,
            address = address,
            dateOfBrith = dateOfBrith,
            accountPassword = passwordEncoder.encode(password),
            createdAt = DateUtil.timeInstant(),
            updatedAt = DateUtil.timeInstant()
        )
    }
}