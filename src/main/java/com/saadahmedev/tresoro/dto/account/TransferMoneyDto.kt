package com.saadahmedev.tresoro.dto.account

import com.fasterxml.jackson.annotation.JsonProperty

data class TransferMoneyDto(
    @JsonProperty("from_account_number")
    val fromAccountNumber: String? = null,
    @JsonProperty("to_account_number")
    val toAccountNumber: String? = null,
    val amount: String? = null
)
