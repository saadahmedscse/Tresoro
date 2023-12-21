package com.saadahmedev.tresoro.securityFilter

import com.saadahmedev.tresoro.security.JwtService
import com.saadahmedev.tresoro.util.DateUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Value("\${tresoro.admin.secret-key}")
    private lateinit var adminSecretKey: String

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath == "/api/auth/create-admin") {
            val header: String? = request.getHeader("X-ADMIN-SECRET-KEY")
            if (header == null || header != adminSecretKey) {
                sendUnauthorizedResponse(
                    response,
                    statusCode = HttpStatus.FORBIDDEN.value(),
                    message = "You don't have permission to access this resource")
                return
            }
        }

        val authorizationHeader: String? = request.getHeader("Authorization")
        var username: String? = null
        var accessToken: String? = null

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && authorizationHeader.length > 7) {
            accessToken = authorizationHeader.substring(7)

            try {
                username = jwtService.getUsername(accessToken)

            } catch (e: Exception) {
                sendUnauthorizedResponse(response)
                return
            }
        }

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            if (jwtService.isExpired(requireNotNull(accessToken))) {
                sendUnauthorizedResponse(response)
                return
            } else {
                val authenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authenticationToken
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun sendUnauthorizedResponse(
        response: HttpServletResponse,
        statusCode: Int = HttpStatus.UNAUTHORIZED.value(),
        message: String = "Token is not valid"
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = statusCode

        response.writer.format(
            """
                {
                   "statusCode": %d,
                   "status": %b,
                   "message": "%s",
                   "timeStamp": "%s"
                }
            """,
            statusCode,
            false,
            message,
            DateUtil.timeInstant()
        )
    }
}