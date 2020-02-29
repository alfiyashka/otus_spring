package ru.avalieva.otus.library_hw12_security.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.avalieva.otus.library_hw12_security.domain.User;
import ru.avalieva.otus.library_hw12_security.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService  {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    private User getUser(String login) {
        try {
            return userService.getUser(login);
        }
        catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        User user = getUser(login);

        Set<GrantedAuthority> roles = new HashSet();
        UserRole role = login.equals("admin") ? UserRole.ADMIN : UserRole.USER;

        roles.add(new SimpleGrantedAuthority(role.name()));

        return new LibraryUserDetails(user, role);
    }

}
