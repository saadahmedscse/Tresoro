package com.saadahmedev.tresoro.service.account

import com.saadahmedev.tresoro.dto.account.AccountDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
interface AccountService {

    fun openAccount(accountDto: AccountDto?): ResponseEntity<*>

    fun updateAccountInfo(id: Long, accountDto: AccountDto?): ResponseEntity<*>

    fun getAccount(id: Long, userId: Long): ResponseEntity<*>

    fun getAccounts(userId: Long): ResponseEntity<*>

    fun closeAccount(id: Long): ResponseEntity<*>
}