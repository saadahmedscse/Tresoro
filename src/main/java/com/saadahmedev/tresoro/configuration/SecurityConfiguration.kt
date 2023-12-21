package com.saadahmedev.tresoro.configuration

import com.saadahmedev.tresoro.securityFilter.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
open class SecurityConfiguration {

    @Autowired
    private lateinit var authenticationEntryPoint: AuthenticationEntryPoint

    @Autowired
    private lateinit var filter: JwtAuthenticationFilter

    @Bean
    @Throws(Exception::class)
    open fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain? {
        return httpSecurity.csrf { t -> t?.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("api/auth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
            }
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    open fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager
}