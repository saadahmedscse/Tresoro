package com.saadahmedev.tresoro.service.transaction

import com.saadahmedev.tresoro.dto.transaction.TransactionDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
interface TransactionService {

    fun initiateTransaction(transactionDto: TransactionDto?): ResponseEntity<*>
}