package com.saadahmedev.tresoro.entity.account

import com.saadahmedev.tresoro.repository.account.AccountStatus
import com.saadahmedev.tresoro.repository.account.AccountType
import com.saadahmedev.tresoro.repository.account.Currency
import jakarta.persistence.*

@Entity
@Table(name = "table_account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "account_number", nullable = false, updatable = false, unique = true)
    var accountNumber: String? = null,
    @Column(nullable = false)
    var balance: String? = null,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var currency: Currency? = null,
    @Column(name = "account_type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    var accountType: AccountType? = null,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: AccountStatus? = null,
    @Column(name = "interest_rate", nullable = false)
    var interestRate: Double? = null,

    @Column(name = "date_opened", nullable = false, updatable = false)
    var dateOpened: String? = null,
    @Column(name = "last_transaction_date")
    var lastTransactionDate: String? = null,

    @Column(name = "customer_id", nullable = false, updatable = false)
    var customerId: Long? = null,
    @Column(name = "branch_id", nullable = false, updatable = false)
    var branchId: Long? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: String? = null,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: String? = null
)
