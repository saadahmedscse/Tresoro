package com.saadahmedev.tresoro.service.validator.account

import com.saadahmedev.springboot.dto.ApiResponse
import com.saadahmedev.springboot.server.ServerResponse
import com.saadahmedev.springboot.server.ServerResponse.badRequest
import com.saadahmedev.tresoro.dto.account.AccountDto
import com.saadahmedev.tresoro.dto.account.AddWithdrawMoneyDto
import com.saadahmedev.tresoro.dto.account.TransferMoneyDto
import com.saadahmedev.tresoro.entity.account.Account
import com.saadahmedev.tresoro.repository.account.AccountRepository
import com.saadahmedev.tresoro.entity.account.AccountStatus
import com.saadahmedev.tresoro.entity.transaction.TransactionType
import com.saadahmedev.tresoro.repository.branch.BranchRepository
import com.saadahmedev.tresoro.repository.user.UserRepository
import com.saadahmedev.tresoro.service.validator.RequestValidator
import com.saadahmedev.tresoro.service.validator.ResponseWithResult
import com.saadahmedev.tresoro.util.Constants
import com.saadahmedev.tresoro.util.isDouble
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
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
        val optionalAccount = accountRepository.findByIdAndCustomerId(id, userId)

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

    fun isTransferMoneyRequestValid(
        transferMoneyDto: TransferMoneyDto?,
        userId: Long? = null
    ): ResponseWithResult<ResponseEntity<ApiResponse>, ResponseWithResult<Account, Account>> {
        if (transferMoneyDto == null) return generateBadRequestForTwoAccounts(message = "Transfer money required body is required")
        if (transferMoneyDto.fromAccountNumber.isNullOrBlank()) return generateBadRequestForTwoAccounts(message = "From account number is required")
        if (transferMoneyDto.toAccountNumber.isNullOrBlank()) return generateBadRequestForTwoAccounts(message = "To account number is required")
        if (transferMoneyDto.amount.isNullOrBlank()) return generateBadRequestForTwoAccounts(message = "Transfer amount is required")

        val optionalFromAccount = if (userId != null) {
            accountRepository.findByAccountNumberAndCustomerId(transferMoneyDto.fromAccountNumber, userId)
        } else {
            accountRepository.findByAccountNumber(transferMoneyDto.fromAccountNumber)
        }

        if (optionalFromAccount.isEmpty) {
            val fromAccountNotFoundMessage =
                if (userId != null) "You don't have any account with the account number ${transferMoneyDto.fromAccountNumber}"
                else "No account found with the account number ${transferMoneyDto.fromAccountNumber}"

            return generateBadRequestForTwoAccounts(message = fromAccountNotFoundMessage)
        }

        val optionalToAccount = accountRepository.findByAccountNumber(transferMoneyDto.toAccountNumber)
        if (optionalToAccount.isEmpty) return generateBadRequestForTwoAccounts(message = "No account found with the account number ${transferMoneyDto.toAccountNumber}")

        val fromAccount = optionalFromAccount.get()
        val toAccount = optionalToAccount.get()

        if (fromAccount.status == AccountStatus.INACTIVE) return generateBadRequestForTwoAccounts(message = "${fromAccount.accountNumber} account is inactive. Transaction is not possible")
        if (fromAccount.status == AccountStatus.CLOSED) return generateBadRequestForTwoAccounts(message = "${fromAccount.accountNumber} account is closed. Transaction is not possible")
        if (toAccount.status == AccountStatus.INACTIVE) return generateBadRequestForTwoAccounts(message = "${fromAccount.accountNumber} account is inactive. Transaction is not possible")
        if (toAccount.status == AccountStatus.CLOSED) return generateBadRequestForTwoAccounts(message = "${fromAccount.accountNumber} account is closed. Transaction is not possible")
        if ((fromAccount.balance?.toDouble()
                ?: 0.0) < transferMoneyDto.amount.toDouble()
        ) return generateBadRequestForTwoAccounts(
            message = "Insufficient balance. Available account balance is ${fromAccount.balance}"
        )

        return ResponseWithResult(
            ServerResponse.ok(),
            ResponseWithResult(
                fromAccount,
                toAccount
            )
        )
    }

    fun isAddMoneyOrWithdrawRequestValid(
        addWithdrawMoneyDto: AddWithdrawMoneyDto?,
        transactionType: TransactionType,
        userId: Long? = null
    ): ResponseWithResult<ResponseEntity<ApiResponse>, Account> {
        if (addWithdrawMoneyDto == null) return generateBadRequestForOneAccount(message = "Transfer money required body is required")
        if (addWithdrawMoneyDto.accountNumber.isNullOrBlank()) return generateBadRequestForOneAccount(message = "Account number is required")
        if (addWithdrawMoneyDto.amount.isNullOrBlank()) return generateBadRequestForOneAccount(message = "Amount is required")
        if (!addWithdrawMoneyDto.amount.isDouble()) return generateBadRequestForOneAccount(message = "Invalid amount provided, only number will be accepted")

        val optionalAccount =
            if (userId != null) accountRepository.findByAccountNumberAndCustomerId(
                addWithdrawMoneyDto.accountNumber,
                userId
            )
            else accountRepository.findByAccountNumber(addWithdrawMoneyDto.accountNumber)

        if (optionalAccount.isEmpty) {
            val fromAccountNotFoundMessage =
                if (userId != null) "You don't have any account with the account number ${addWithdrawMoneyDto.accountNumber}"
                else "No account found with the account number ${addWithdrawMoneyDto.accountNumber}"

            return generateBadRequestForOneAccount(fromAccountNotFoundMessage)
        }

        if (transactionType == TransactionType.WITHDRAW) {
            if (optionalAccount.get().balance!!.toDouble() < addWithdrawMoneyDto.amount.toDouble()) return generateBadRequestForOneAccount(
                message = "Insufficient balance. Available account balance is ${optionalAccount.get().balance}"
            )
        }

        return ResponseWithResult(
            ServerResponse.ok(),
            optionalAccount.get()
        )
    }

    private fun generateBadRequestForTwoAccounts(message: String): ResponseWithResult<ResponseEntity<ApiResponse>, ResponseWithResult<Account, Account>> {
        return ResponseWithResult(
            var1 = badRequest(message = message),
            var2 = null
        )
    }

    private fun generateBadRequestForOneAccount(message: String): ResponseWithResult<ResponseEntity<ApiResponse>, Account> {
        return ResponseWithResult(
            var1 = badRequest(message = message),
            var2 = null
        )
    }
}