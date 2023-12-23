package com.saadahmedev.tresoro.repository.transaction

import com.saadahmedev.tresoro.entity.transaction.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
}