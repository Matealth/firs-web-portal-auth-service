package com.firs.risk.mgt.auth.svc.security.userdetails;

import com.firs.risk.mgt.auth.svc.entity.User;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Builder
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private UUID ID;
    private String username;
    private String password;
    private boolean isActive;
    private boolean isEnabled;

    private Collection<SimpleGrantedAuthority> authorities;

    public UserDetailsImpl(UUID ID, String username, String password, boolean isActive,
                           boolean isEnabled, Collection<SimpleGrantedAuthority> authorities) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role->  authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new UserDetailsImpl(
                user.getID(),
                user.getEmail(),
                user.getPassword(),
                user.getEnabled(),
                user.getActive(),
                authorities);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UUID getID() {
        return ID;
    }



    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(ID, user.ID);
    }
}
