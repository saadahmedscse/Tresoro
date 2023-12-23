package com.saadahmedev.tresoro.service.account

import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.tresoro.dto.account.AccountDto
import com.saadahmedev.tresoro.dto.account.AddWithdrawMoneyDto
import com.saadahmedev.tresoro.dto.account.TransferMoneyDto
import com.saadahmedev.tresoro.dto.transaction.TransactionDto
import com.saadahmedev.tresoro.dto.user.UserDto
import com.saadahmedev.tresoro.entity.account.AccountStatus
import com.saadahmedev.tresoro.entity.transaction.TransactionType
import com.saadahmedev.tresoro.entity.user.User
import com.saadahmedev.tresoro.repository.account.AccountRepository
import com.saadahmedev.tresoro.repository.branch.BranchRepository
import com.saadahmedev.tresoro.repository.user.UserRepository
import com.saadahmedev.tresoro.service.transaction.TransactionService
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.service.validator.account.AccountRequestValidator
import com.saadahmedev.tresoro.util.Constants
import com.saadahmedev.tresoro.util.DateUtil
import com.saadahmedev.tresoro.util.getTransactionNumber
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
open class AccountServiceImpl : AccountService {

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var branchRepository: BranchRepository

    @Autowired
    private lateinit var transactionService: TransactionService

    @Autowired
    @Qualifier(Constants.BeanQualifier.ACCOUNT_REQUEST_VALIDATOR)
    private lateinit var accountValidator: AccountRequestValidator

    @Autowired
    @Qualifier(Constants.BeanQualifier.USER_REQUEST_VALIDATOR)
    private lateinit var userValidator: RequestValidator<User, UserDto, Long, UserRepository>

    override fun openAccount(accountDto: AccountDto?): ResponseEntity<*> {
        val validationResult = accountValidator.isCreateRequestValid(accountDto)
        if (validationResult.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return validationResult

        return try {
            accountRepository.save(
                requireNotNull(
                    accountDto?.toEntity(
                        customer = userRepository.findById(requireNotNull(accountDto.customerId)).get(),
                        branch = branchRepository.findById(requireNotNull(accountDto.branchId)).get()
                    )
                )
            )

            ServerResponse.created(message = "Account has been opened successfully")
        } catch (e: Exception) {
            ServerResponse.internalServerError(exception = e)
        }
    }

    @Transactional
    override fun updateAccountInfo(id: Long, accountDto: AccountDto?): ResponseEntity<*> {
        val validationResult = accountValidator.isUpdateRequestValid(id, accountDto, accountRepository)
        if (validationResult.var1.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return validationResult.var1

        val account = requireNotNull(validationResult.var2)
        requireNotNull(accountDto)

        if (accountDto.currency != null) account.currency = accountDto.currency
        if (accountDto.interestRate != null) account.interestRate = accountDto.interestRate
        account.updatedAt = DateUtil.timeInstant()

        return try {
            accountRepository.save(account)
            ServerResponse.ok(message = "Account information has been updated successfully")
        } catch (e: Exception) {
            ServerResponse.internalServerError(exception = e)
        }
    }

    override fun getAccount(id: Long, userId: Long): ResponseEntity<*> {
        val existenceResult = accountValidator.isAccountExist(id, userId)
        if (existenceResult.var1.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return existenceResult.var1

        return ServerResponse.body(requireNotNull(existenceResult.var2?.toResponse()))
    }

    override fun getAccounts(userId: Long): ResponseEntity<*> {
        val existenceResult = userValidator.isExist(userId, userRepository)
        if (existenceResult.var1.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return existenceResult.var1

        return ServerResponse.body(accountRepository.findAllByCustomerId(userId).map { it.toResponse() })
    }

    @Transactional
    override fun changeStatus(id: Long, status: AccountStatus): ResponseEntity<*> {
        val existenceResult = accountValidator.isExist(id, accountRepository)
        if (existenceResult.var1.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return existenceResult.var1

        return try {
            accountRepository.save(
                existenceResult.var2.apply {
                    this?.status = status
                }!!
            )

            ServerResponse.ok(message = "Account has been changed to ${status.name} successfully")
        } catch (e: Exception) {
            ServerResponse.internalServerError(exception = e)
        }
    }

    @Transactional
    override fun transferMoney(transferMoneyDto: TransferMoneyDto?, userId: Long?): ResponseEntity<*> {
        val validationResult = accountValidator.isTransferMoneyRequestValid(transferMoneyDto, userId)
        if (validationResult.var1.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return validationResult.var1

        val fromAccount = validationResult.var2!!.var1
        val toAccount = validationResult.var2.var2!!

        fromAccount.balance = (fromAccount.balance!!.toDouble() - transferMoneyDto!!.amount!!.toDouble()).toString()
        toAccount.balance = (toAccount.balance!!.toDouble() + transferMoneyDto.amount!!.toDouble()).toString()
        fromAccount.updatedAt = DateUtil.timeInstant()
        fromAccount.lastTransactionDate = DateUtil.timeInstant()
        toAccount.updatedAt = DateUtil.timeInstant()

        return try {
            accountRepository.save(fromAccount)
            accountRepository.save(toAccount)

            val transactionNumber = getTransactionNumber()

            transactionService.initiateTransaction(
                TransactionDto(
                    transactionNumber = transactionNumber,
                    account = toAccount,
                    amount = transferMoneyDto.amount,
                    transactionType = TransactionType.RECEIVED
                )
            )

            transactionService.initiateTransaction(
                TransactionDto(
                    transactionNumber = transactionNumber,
                    account = fromAccount,
                    amount = transferMoneyDto.amount,
                    transactionType = TransactionType.TRANSFER
                )
            )
        } catch (e: Exception) {
            throw e
        }
    }

    @Transactional
    override fun addOrWithdrawMoney(
        addWithdrawMoneyDto: AddWithdrawMoneyDto?,
        transactionType: TransactionType,
        userId: Long?
    ): ResponseEntity<*> {
        val validationResult =
            accountValidator.isAddMoneyOrWithdrawRequestValid(addWithdrawMoneyDto, transactionType, userId)
        if (validationResult.var1.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return validationResult.var1

        val account = validationResult.var2!!
        account.updatedAt = DateUtil.timeInstant()
        account.balance =
            when (transactionType) {
                TransactionType.ADD_BALANCE -> (account.balance?.toDouble()!! + addWithdrawMoneyDto!!.amount?.toDouble()!!).toString()
                TransactionType.WITHDRAW -> (account.balance?.toDouble()!! - addWithdrawMoneyDto!!.amount?.toDouble()!!).toString()
                else -> account.balance
            }

        return try {
            accountRepository.save(account)

            transactionService.initiateTransaction(
                TransactionDto(
                    transactionNumber = getTransactionNumber(),
                    account = account,
                    amount = addWithdrawMoneyDto!!.amount,
                    transactionType = transactionType
                )
            )
        } catch (e: Exception) {
            throw e
        }
    }
}