package com.saadahmedev.tresoro.repository.user

import com.saadahmedev.tresoro.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): Optional<User>

    fun findByPhone(phone: String): Optional<User>
}