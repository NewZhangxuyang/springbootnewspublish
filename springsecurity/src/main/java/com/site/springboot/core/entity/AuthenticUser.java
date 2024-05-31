package com.site.springboot.core.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticUser implements java.io.Serializable, UserDetails {
    private Integer userId;
    private String username;
    private String password;
    private Integer enabled;
    private Integer accountNoExpired;
    private Integer accountNoLocked;
    private Integer credentialsNoExpired;
    private List<? extends GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNoExpired.equals(1);
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNoExpired.equals(1);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNoExpired.equals(1);
    }

    @Override
    public boolean isEnabled() {
        return this.enabled.equals(1);
    }
}
