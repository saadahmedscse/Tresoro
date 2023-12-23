package com.saadahmedev.tresoro.service.account

import com.saadahmedev.tresoro.dto.account.AccountDto
import com.saadahmedev.tresoro.dto.account.AddWithdrawMoneyDto
import com.saadahmedev.tresoro.dto.account.TransferMoneyDto
import com.saadahmedev.tresoro.entity.account.AccountStatus
import com.saadahmedev.tresoro.entity.transaction.TransactionType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
interface AccountService {

    fun openAccount(accountDto: AccountDto?): ResponseEntity<*>

    fun updateAccountInfo(id: Long, accountDto: AccountDto?): ResponseEntity<*>

    fun getAccount(id: Long, userId: Long): ResponseEntity<*>

    fun getAccounts(userId: Long): ResponseEntity<*>

    fun changeStatus(id: Long, status: AccountStatus): ResponseEntity<*>

    fun transferMoney(transferMoneyDto: TransferMoneyDto?, userId: Long? = null): ResponseEntity<*>

    fun addOrWithdrawMoney(addWithdrawMoneyDto: AddWithdrawMoneyDto?, transactionType: TransactionType, userId: Long? = null): ResponseEntity<*>
}