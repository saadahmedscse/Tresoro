package com.saadahmedev.tresoro.controller.account

import com.saadahmedev.tresoro.dto.account.AccountDto
import com.saadahmedev.tresoro.security.JwtService
import com.saadahmedev.tresoro.service.account.AccountService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
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
    open fun updateAccount(@PathVariable("id") id: Long, @RequestBody accountDto: AccountDto?) = accountService.updateAccountInfo(id, accountDto)

    @GetMapping("{id}")
    open fun getAccount(@PathVariable("id") id: Long, request: HttpServletRequest) =
        jwtService.getUserId(getTokenFromRequest(request))?.let { accountService.getAccount(id, it) }

    @GetMapping
    open fun getAccounts(request: HttpServletRequest) =
        jwtService.getUserId(getTokenFromRequest(request))?.let { accountService.getAccounts(it) }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    open fun closeAccount(@PathVariable("id") id: Long) = accountService.closeAccount(id)

    private fun getTokenFromRequest(request: HttpServletRequest): String {
        return request.getHeader("Authorization").substring(7)
    }
}