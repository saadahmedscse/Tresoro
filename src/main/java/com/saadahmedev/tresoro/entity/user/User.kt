package com.saadahmedev.tresoro.entity.user

import com.saadahmedev.tresoro.dto.user.UserResponse
import com.saadahmedev.tresoro.util.ifNullOrBlank
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "table_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "first_name", nullable = false)
    var firstName: String? = null,
    @Column(name = "last_name", nullable = false)
    var lastName: String? = null,
    @Column(name = "full_name", nullable = false)
    var fullName: String? = null,
    @Column(unique = true, nullable = false, updatable = false)
    var email: String? = null,
    @Column(unique = true, nullable = false, updatable = false)
    var phone: String? = null,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: UserType? = null,
    @Column(nullable = false)
    var address: String? = null,
    @Column(name = "date_of_birth", nullable = false, updatable = false)
    var dateOfBrith: String? = null,
    @Column(name = "password", nullable = false)
    var accountPassword: String? = null,
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: String? = null,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: String? = null
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority(this.role?.name))

    override fun getPassword(): String = this.accountPassword.ifNullOrBlank { "" }

    override fun getUsername(): String = this.email.ifNullOrBlank { "" }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    fun toUserResponse(): UserResponse {
        return UserResponse(
            id = id,
            firstName = firstName,
            lastName = lastName,
            fullName = fullName,
            email = email,
            phone = phone,
            role = role,
            address = address,
            dateOfBrith = dateOfBrith
        )
    }
}
