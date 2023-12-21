package com.saadahmedev.tresoro.security

import com.saadahmedev.tresoro.repository.user.UserRepository
import com.saadahmedev.tresoro.util.ifNullOrBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {
        val optionalUser = userRepository.findByEmail(username.ifNullOrBlank { "" })
        if (optionalUser.isEmpty) throw UsernameNotFoundException("$username not found as a username in server")
        return optionalUser.get()
    }
}