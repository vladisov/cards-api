package dev.cards.dto

import dev.cards.domain.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors


/**
 * @author vladov 2019-03-31
 */
class UserDto(private var id: String,
              private var username: String,
              private var password: String,
              private var enabled: Boolean,
              private var roles: List<Role>) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.roles.stream().map { authority ->
            SimpleGrantedAuthority(authority.name)
        }.collect(Collectors.toList())
    }

    override fun isEnabled(): Boolean {
        return this.enabled
    }

    override fun getUsername(): String {
        return this.username
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    fun roles(): List<Role> {
        return this.roles;
    }

    fun id(): String {
        return this.id;
    }
}
