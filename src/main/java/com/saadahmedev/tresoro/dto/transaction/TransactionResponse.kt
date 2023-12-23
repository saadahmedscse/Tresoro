package com.saadahmedev.tresoro.dto.transaction

import com.fasterxml.jackson.annotation.JsonProperty
import com.saadahmedev.tresoro.dto.account.AccountResponse
import com.saadahmedev.tresoro.entity.transaction.TransactionType

data class TransactionResponse(
    var id: Long? = null,
    @JsonProperty("transaction_number")
    var transactionNumber: String? = null,
    @JsonProperty("account_id")
    var account: AccountResponse? = null,
    var amount: String? = null,
    @JsonProperty("transaction_type")
    var transactionType: TransactionType? = null,
    @JsonProperty("created_at")
    var createdAt: String? = null,
    @JsonProperty("updated_at")
    var updatedAt: String? = null
)
