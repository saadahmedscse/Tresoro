package com.saadahmedev.tresoro.controller.account

import com.saadahmedev.tresoro.dto.account.AccountDto
import com.saadahmedev.tresoro.dto.account.AddWithdrawMoneyDto
import com.saadahmedev.tresoro.dto.account.TransferMoneyDto
import com.saadahmedev.tresoro.entity.account.AccountStatus
import com.saadahmedev.tresoro.entity.transaction.TransactionType
import com.saadahmedev.tresoro.security.JwtService
import com.saadahmedev.tresoro.service.account.AccountService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/account")
open class AccountController {

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var jwtService: JwtService

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    open fun openAccount(@RequestBody accountDto: AccountDto?) = accountService.openAccount(accountDto)

    @PostMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    open fun updateAccount(@PathVariable("id") id: Long, @RequestBody accountDto: AccountDto?) =
        accountService.updateAccountInfo(id, accountDto)

    @GetMapping("{id}")
    open fun getAccount(@PathVariable("id") id: Long, request: HttpServletRequest) =
        jwtService.getUserId(getTokenFromRequest(request))?.let { accountService.getAccount(id, it) }

    @GetMapping
    open fun getAccounts(request: HttpServletRequest) =
        jwtService.getUserId(getTokenFromRequest(request))?.let { accountService.getAccounts(it) }

    @PostMapping("close/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    open fun closeAccount(@PathVariable("id") id: Long) = accountService.changeStatus(id, AccountStatus.CLOSED)

    @PostMapping("inactive/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    open fun inactiveAccount(@PathVariable("id") id: Long) = accountService.changeStatus(id, AccountStatus.INACTIVE)

    @PostMapping("transfer")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    open fun transferMoney(@RequestBody transferMoneyDto: TransferMoneyDto?) =
        accountService.transferMoney(transferMoneyDto)

    @PostMapping("send")
    open fun sendMoney(@RequestBody transferMoneyDto: TransferMoneyDto?, request: HttpServletRequest) =
        jwtService.getUserId(getTokenFromRequest(request))?.let { accountService.transferMoney(transferMoneyDto, it) }

    @PostMapping("balance-add")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    open fun balanceAdd(@RequestBody addWithdrawMoneyDto: AddWithdrawMoneyDto?) =
        accountService.addOrWithdrawMoney(addWithdrawMoneyDto, TransactionType.ADD_BALANCE)

    @PostMapping("deposit")
    open fun deposit(@RequestBody addWithdrawMoneyDto: AddWithdrawMoneyDto?, request: HttpServletRequest) =
        jwtService.getUserId(getTokenFromRequest(request))
            ?.let { accountService.addOrWithdrawMoney(addWithdrawMoneyDto, TransactionType.ADD_BALANCE, it) }

    @PostMapping("balance-withdraw")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    open fun balanceWithdraw(@RequestBody addWithdrawMoneyDto: AddWithdrawMoneyDto?) =
        accountService.addOrWithdrawMoney(addWithdrawMoneyDto, TransactionType.WITHDRAW)

    @PostMapping("withdraw")
    open fun withdraw(@RequestBody addWithdrawMoneyDto: AddWithdrawMoneyDto?, request: HttpServletRequest) =
        jwtService.getUserId(getTokenFromRequest(request))
            ?.let { accountService.addOrWithdrawMoney(addWithdrawMoneyDto, TransactionType.WITHDRAW, it) }

    private fun getTokenFromRequest(request: HttpServletRequest): String {
        return request.getHeader("Authorization").substring(7)
    }
}