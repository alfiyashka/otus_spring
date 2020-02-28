package ru.avalieva.otus.library_hw12_security.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import ru.avalieva.otus.library_hw12_security.domain.User;

import java.util.Collection;

public class LibraryUserDetails implements UserDetails {
    private final User user;
    private final UserRole role;

    public LibraryUserDetails(User user, UserRole role) {
        this.user = user;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  AuthorityUtils.createAuthorityList(role == UserRole.ADMIN ? "ROLE_ADMIN" : "ROLE_USER");
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
