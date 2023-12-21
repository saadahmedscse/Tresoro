package com.saadahmedev.tresoro.dto.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.saadahmedev.tresoro.entity.user.UserType

data class UserResponse(
    var id: Long? = null,
    @JsonProperty("first_name")
    var firstName: String? = null,
    @JsonProperty("last_name")
    var lastName: String? = null,
    @JsonProperty("full_name")
    var fullName: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var role: UserType? = null,
    var address: String? = null,
    @JsonProperty("date_of_birth")
    var dateOfBrith: String? = null
)