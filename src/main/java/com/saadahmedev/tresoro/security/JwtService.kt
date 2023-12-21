package com.saadahmedev.tresoro.security

import com.saadahmedev.tresoro.dto.user.UserResponse
import com.saadahmedev.tresoro.repository.user.UserRepository
import com.saadahmedev.tresoro.util.ifNullOrBlank
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.function.Function
import kotlin.collections.HashMap

@Component
class JwtService {

    @Value("\${tresoro.jwt.secret-key}")
    private lateinit var jwtSecretKey: String

    @Autowired
    private lateinit var userRepository: UserRepository

    fun generateAccessToken(username: String): String {
        val claims = hashMapOf<String, Any>()
        setupClaims(claims, username)
        return generateAccessToken(claims, username)
    }

    fun isExpired(token: String): Boolean = getExpirationDateFromToken(token).before(Date())

    fun getUsername(token: String): String = getClaimFromToken(token, Claims::getSubject)

    fun getUser(token: String): UserResponse? = getClaimsFromToken(token)["user"] as UserResponse?

    fun getUserRole(token: String): String? = getClaimsFromToken(token)["authority"] as String?

    fun getUserId(token: String): Long? = getUser(token)?.id

    private fun setupClaims(claims: HashMap<String, Any>, username: String) {
        val simplifiedUserWrapper = userRepository.findByEmail(username).get().toUserResponse()
        claims["user"] = simplifiedUserWrapper
        claims["authority"] = simplifiedUserWrapper.role?.name.ifNullOrBlank { "" }
    }

    private fun generateAccessToken(claims: Map<String, Any>, username: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuer("Tresoro")
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis().plus(7L * 24 * 60 * 60 * 1000)))
            .signWith(getSigningKey())
            .compact()
    }

    private fun getExpirationDateFromToken(token: String): Date = getClaimFromToken(token, Claims::getExpiration)

    private fun <T> getClaimFromToken(token: String, claimResolver: Function<Claims, T>): T =
        claimResolver.apply(getClaimsFromToken(token))

    private fun getClaimsFromToken(token: String): Claims =
        Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).body

    private fun getSigningKey(): Key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecretKey))
}