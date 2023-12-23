package com.saadahmedev.tresoro.service.validator.transaction

import com.saadahmedev.springboot.dto.ApiResponse
import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.springboot.server.ServerResponse.badRequest
import com.saadahmedev.tresoro.dto.transaction.TransactionDto
import com.saadahmedev.tresoro.entity.transaction.Transaction
import com.saadahmedev.tresoro.repository.transaction.TransactionRepository
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.util.Constants
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Qualifier(Constants.BeanQualifier.TRANSACTION_REQUEST_VALIDATOR)
class TransactionRequestValidator : RequestValidator<Transaction, TransactionDto, Long, TransactionRepository>() {

    override fun isCreateRequestValid(body: TransactionDto?): ResponseEntity<ApiResponse> {
        return when {
            body == null -> badRequest(message = "Transaction request body is required")
            body.transactionNumber.isNullOrBlank() -> badRequest(message = "Transaction number is required")
            body.account == null -> badRequest(message = "Transaction account is required")
            body.amount.isNullOrBlank() -> badRequest(message = "Transaction amount is required")
            body.transactionType == null -> badRequest(message = "Transaction type is required")

            else -> ServerResponse.ok()
        }
    }
}