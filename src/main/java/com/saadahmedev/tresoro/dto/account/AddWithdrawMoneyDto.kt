package com.saadahmedev.tresoro.dto.account

import com.fasterxml.jackson.annotation.JsonProperty

data class AddWithdrawMoneyDto(
    @JsonProperty("account_number")
    val accountNumber: String? = null,
    val amount: String? = null
)
