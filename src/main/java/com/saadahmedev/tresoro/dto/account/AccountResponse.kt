package com.saadahmedev.tresoro.dto.account

import com.fasterxml.jackson.annotation.JsonProperty
import com.saadahmedev.tresoro.dto.user.UserResponse
import com.saadahmedev.tresoro.entity.branch.Branch
import com.saadahmedev.tresoro.entity.account.AccountStatus
import com.saadahmedev.tresoro.entity.account.AccountType
import com.saadahmedev.tresoro.entity.account.Currency

data class AccountResponse (
    val id: Long? = null,
    @JsonProperty("account_number")
    val accountNumber: String? = null,
    val balance: String? = null,
    val currency: Currency? = null,
    @JsonProperty("account_type")
    val accountType: AccountType? = null,
    val status: AccountStatus? = null,
    @JsonProperty("interest_rate")
    val interestRate: Double? = null,
    @JsonProperty("date_opened")
    val dateOpened: String? = null,
    @JsonProperty("last_transaction_date")
    val lastTransactionDate: String? = null,
    val customer: UserResponse? = null,
    val branch: Branch? = null,
    @JsonProperty("created_at")
    val createdAt: String? = null,
    @JsonProperty("updated_at")
    val updatedAt: String? = null
)