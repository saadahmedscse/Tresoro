package com.saadahmedev.tresoro.service.account

import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.tresoro.dto.account.AccountDto
import com.saadahmedev.tresoro.dto.user.UserDto
import com.saadahmedev.tresoro.entity.account.Account
import com.saadahmedev.tresoro.entity.user.User
import com.saadahmedev.tresoro.repository.account.AccountRepository
import com.saadahmedev.tresoro.repository.user.UserRepository
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.service.validator.account.AccountRequestValidator
import com.saadahmedev.tresoro.util.Constants
import com.saadahmedev.tresoro.util.DateUtil
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
    @Qualifier(Constants.BeanQualifier.ACCOUNT_REQUEST_VALIDATOR)
    private lateinit var accountValidator: AccountRequestValidator

    @Autowired
    @Qualifier(Constants.BeanQualifier.USER_REQUEST_VALIDATOR)
    private lateinit var userValidator: RequestValidator<User, UserDto, Long, UserRepository>

    override fun openAccount(accountDto: AccountDto?): ResponseEntity<*> {
        val validationResult = accountValidator.isCreateRequestValid(accountDto)
        if (validationResult.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return validationResult

        return try {
            accountRepository.save(requireNotNull(accountDto?.toEntity()))
            ServerResponse.created(message = "Account has been opened successfully")
        } catch (e: Exception) {
            ServerResponse.internalServerError(exception = e)
        }
    }

    @Transactional
    override fun updateAccountInfo(id: Long, accountDto: AccountDto?): ResponseEntity<*> {
        val validationResult = accountValidator.isUpdateRequestValid(id, accountDto, accountRepository)
        if (validationResult.response.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return validationResult.response

        val account = requireNotNull(validationResult.result)
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
        if (existenceResult.response.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return existenceResult.response

        return ServerResponse.body(requireNotNull(existenceResult.result))
    }

    override fun getAccounts(userId: Long): ResponseEntity<*> {
        val existenceResult = userValidator.isExist(userId, userRepository)
        if (existenceResult.response.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return existenceResult.response

        return ServerResponse.body(accountRepository.findAllByCustomerId(userId))
    }

    @Transactional
    override fun closeAccount(id: Long): ResponseEntity<*> {
        val existenceResult = accountValidator.isExist(id, accountRepository)
        if (existenceResult.response.statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) return existenceResult.response

        return try {
            accountRepository.deleteById(id)
            ServerResponse.ok(message = "Account has been deleted successfully")
        } catch (e: Exception) {
            ServerResponse.internalServerError(exception = e)
        }
    }
}