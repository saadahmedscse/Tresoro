package com.saadahmedev.tresoro.entity.transaction

import com.saadahmedev.tresoro.dto.transaction.TransactionResponse
import com.saadahmedev.tresoro.entity.account.Account
import jakarta.persistence.*

@Entity
@Table(name = "table_transaction")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "transaction_number", nullable = false, updatable = false)
    var transactionNumber: String? = null,
    @ManyToOne
    @JoinColumn(name = "account_id", updatable = false)
    var account: Account? = null,
    @Column(nullable = false, updatable = false)
    var amount: String? = null,
    @Column(name = "transaction_type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    var transactionType: TransactionType? = null,
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: String? = null,
    @Column(name = "updated_at", nullable = false, updatable = false)
    var updatedAt: String? = null
) {
    fun toResponse(): TransactionResponse {
        return TransactionResponse(
            id = id,
            transactionNumber = transactionNumber,
            account = account?.toResponse(),
            amount = amount,
            transactionType = transactionType,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}