package ru.otus.avaliva.hw17.library.docker.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.avaliva.hw17.library.docker.domain.User;
import ru.otus.avaliva.hw17.library.docker.service.MessageService;
import ru.otus.avaliva.hw17.library.docker.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final MessageService messageService;

    public UserDetailsServiceImpl(UserService userService,
                                  MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        User user = userService.getUser(login).orElseThrow(() -> new UsernameNotFoundException(
                messageService.getMessage("security.unknown.login", login)));

        Set<GrantedAuthority> roles = new HashSet<>();
        UserRole role = login.equals("admin") ? UserRole.ADMIN : UserRole.USER;

        roles.add(new SimpleGrantedAuthority(role.name()));

        return new LibraryUserDetails(user, role);
    }

}

