package com.saadahmedev.tresoro.dto.account

import com.fasterxml.jackson.annotation.JsonProperty
import com.saadahmedev.tresoro.entity.account.Account
import com.saadahmedev.tresoro.repository.account.AccountStatus
import com.saadahmedev.tresoro.repository.account.AccountType
import com.saadahmedev.tresoro.repository.account.Currency
import com.saadahmedev.tresoro.util.DateUtil
import com.saadahmedev.tresoro.util.getUniqueAccountNumber

data class AccountDto(
    var currency: Currency? = null,
    @JsonProperty("account_type")
    var accountType: AccountType? = null,
    @JsonProperty("interest_rate")
    var interestRate: Double? = null,
    @JsonProperty("customer_id")
    var customerId: Long? = null,
    @JsonProperty("branch_id")
    var branchId: Long? = null
) {
    fun toEntity(): Account {
        return Account(
            accountNumber = getUniqueAccountNumber(),
            balance = "0.0",
            currency = currency,
            accountType = accountType,
            status = AccountStatus.ACTIVE,
            interestRate = 0.9,
            dateOpened = DateUtil.timeInstant(),
            customerId = customerId,
            branchId = branchId,
            createdAt = DateUtil.timeInstant(),
            updatedAt = DateUtil.timeInstant()
        )
    }
}
