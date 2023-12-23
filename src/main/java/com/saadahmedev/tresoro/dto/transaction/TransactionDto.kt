package com.saadahmedev.tresoro.dto.transaction

import com.saadahmedev.tresoro.entity.account.Account
import com.saadahmedev.tresoro.entity.transaction.Transaction
import com.saadahmedev.tresoro.entity.transaction.TransactionType
import com.saadahmedev.tresoro.util.DateUtil

data class TransactionDto(
    val transactionNumber: String? = null,
    val account: Account? = null,
    val amount: String? = null,
    val transactionType: TransactionType? = null
) {
    fun toEntity(): Transaction {
        return Transaction(
            transactionNumber = transactionNumber?.uppercase(),
            amount = amount,
            account = account,
            transactionType = transactionType,
            createdAt = DateUtil.timeInstant(),
            updatedAt = DateUtil.timeInstant()
        )
    }
}
