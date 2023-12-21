package com.saadahmedev.tresoro.repository.account

import com.saadahmedev.tresoro.entity.account.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
interface AccountRepository : JpaRepository<Account, Long> {

    fun findAllByCustomerId(userId: Long): List<Account>

    fun findByIdAndCustomerId(id: Long, customerId: Long): Optional<Account>
}