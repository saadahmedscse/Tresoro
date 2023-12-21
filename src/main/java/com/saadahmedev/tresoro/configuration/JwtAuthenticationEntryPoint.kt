package com.saadahmedev.tresoro.configuration

import com.saadahmedev.tresoro.util.DateUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.status = HttpStatus.UNAUTHORIZED.value()

        response?.writer?.format(
            """
                {
                   "statusCode": %d,
                   "status": %b,
                   "message": "%s",
                   "timeStamp": "%s"
                }
            """,
            401,
            false,
            authException?.message?.replace("Bad credentials", "Incorrect password"),
            DateUtil.timeInstant()
        )
    }
}