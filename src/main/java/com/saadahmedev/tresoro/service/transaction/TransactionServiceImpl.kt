package com.saadahmedev.tresoro.service.transaction

import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.tresoro.dto.transaction.TransactionDto
import com.saadahmedev.tresoro.entity.transaction.Transaction
import com.saadahmedev.tresoro.repository.transaction.TransactionRepository
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.util.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TransactionServiceImpl : TransactionService {

    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    @Autowired
    @Qualifier(Constants.BeanQualifier.TRANSACTION_REQUEST_VALIDATOR)
    private lateinit var transactionValidator: RequestValidator<Transaction, TransactionDto, Long, TransactionRepository>

    override fun initiateTransaction(transactionDto: TransactionDto?): ResponseEntity<*> {
        val validationResult = transactionValidator.isCreateRequestValid(transactionDto)
        if (validationResult.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return validationResult

        return ServerResponse.body(transactionRepository.save(requireNotNull(transactionDto?.toEntity())).toResponse(), HttpStatus.CREATED)
    }
}