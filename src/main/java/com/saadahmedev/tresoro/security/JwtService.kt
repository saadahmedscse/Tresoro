package com.saadahmedev.tresoro.security

import com.saadahmedev.tresoro.dto.user.UserResponse
import com.saadahmedev.tresoro.entity.user.UserType
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

    fun getUser(token: String): UserResponse = claimsToUser(getClaimsFromToken(token))

    fun getUserRole(token: String): String? = getUser(token).role?.name

    fun getUserId(token: String): Long? = getUser(token).id

    private fun claimsToUser(claims: Claims): UserResponse {
        val userClaims = claims["user"] as? Map<*, *>
        var userResponse = UserResponse()

        userClaims?.let {
            userResponse = UserResponse(
                id = (it["id"] as Int).toLong(),
                firstName = it["firstName"] as String?,
                lastName = it["lastName"] as String?,
                fullName = it["fullName"] as String?,
                email = it["email"] as String?,
                phone = it["phone"] as String?,
                role =  getUserType(it["role"] as String?),
                address = it["address"] as String?,
                dateOfBrith = it["dateOfBirth"] as String?
            )
        }

        return userResponse
    }

    private fun getUserType(role: String?): UserType? {
        return when (role) {
            "ADMIN" -> UserType.ADMIN
            "MANAGER" -> UserType.MANAGER
            "HR" -> UserType.HR
            "EMPLOYEE" -> UserType.EMPLOYEE
            "CUSTOMER" -> UserType.CUSTOMER
            else -> null
        }
    }

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