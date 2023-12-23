package com.saadahmedev.tresoro.service.validator.account

import com.saadahmedev.springboot.dto.ApiResponse
import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.springboot.server.ServerResponse.badRequest
import com.saadahmedev.tresoro.dto.account.AccountDto
import com.saadahmedev.tresoro.entity.account.Account
import com.saadahmedev.tresoro.repository.account.AccountRepository
import com.saadahmedev.tresoro.repository.branch.BranchRepository
import com.saadahmedev.tresoro.repository.user.UserRepository
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.service.validator.ResponseWithResult
import com.saadahmedev.tresoro.util.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Qualifier(Constants.BeanQualifier.ACCOUNT_REQUEST_VALIDATOR)
class AccountRequestValidator : RequestValidator<Account, AccountDto, Long, AccountRepository>() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var branchRepository: BranchRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    override fun isCreateRequestValid(body: AccountDto?): ResponseEntity<ApiResponse> {
        return when {
            body == null -> badRequest(message = "Request body is required")
            body.currency == null -> badRequest(message = "Primary currency is required")
            body.accountType == null -> badRequest(message = "Account type is required")
            body.customerId == null -> badRequest(message = "Customer id is required")
            userRepository.findById(requireNotNull(body.customerId)).isEmpty -> badRequest(message = "No customer found with the id ${body.customerId}")
            body.branchId == null -> badRequest(message = "Branch id is required")
            branchRepository.findById(requireNotNull(body.branchId)).isEmpty -> badRequest(message = "No branch found with the id ${body.branchId}")

            else -> ServerResponse.ok()
        }
    }

    fun isAccountExist(id: Long, userId: Long): ResponseWithResult<ResponseEntity<ApiResponse>, Account> {
        val optionalAccount = accountRepository.getAccountWithLastTransactionDate(id, userId)

        return if (optionalAccount.isPresent) {
            ResponseWithResult(
                ServerResponse.ok(),
                optionalAccount.get()
            )
        } else ResponseWithResult(
            badRequest(message = "No account found with the id $id and customer id $userId"),
            null
        )
    }
}