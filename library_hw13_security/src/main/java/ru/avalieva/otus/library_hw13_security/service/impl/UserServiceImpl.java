package ru.avalieva.otus.library_hw13_security.service.impl;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.avalieva.otus.library_hw13_security.domain.User;
import ru.avalieva.otus.library_hw13_security.repository.UserRepository;
import ru.avalieva.otus.library_hw13_security.service.MessageService;
import ru.avalieva.otus.library_hw13_security.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository,
                           MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @Override
    public Optional<User> getUser(String login) {
        return userRepository.findByLogin(login);

    }

    @Override
    public void addUser(User user) {
        try {
            User userEncoded = new User(0, user.getLogin(), passwordEncoder.encode(user.getPassword()));
            userRepository.save(userEncoded);
        }
        catch (Exception e) {
            throw new LibraryException(messageService.getMessage("user.add.error",
                    user.getLogin(), e.getMessage()), e);
        }
    }
}
