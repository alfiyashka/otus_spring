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

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService.getUser(login);
        Set<GrantedAuthority> roles = new HashSet();
        UserRole role = login.equals("admin") ? UserRole.ADMIN : UserRole.USER;

        roles.add(new SimpleGrantedAuthority(role.name()));
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(user.getLogin(),
                        user.getPassword(),
                        roles);

        return userDetails;
    }

}
