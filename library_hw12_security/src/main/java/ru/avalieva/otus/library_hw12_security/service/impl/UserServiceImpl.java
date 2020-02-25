package ru.avalieva.otus.library_hw12_security.service.impl;

import org.springframework.stereotype.Service;
import ru.avalieva.otus.library_hw12_security.domain.User;
import ru.avalieva.otus.library_hw12_security.repository.UserRepository;
import ru.avalieva.otus.library_hw12_security.security.MD5PasswordEncoder;
import ru.avalieva.otus.library_hw12_security.service.MessageService;
import ru.avalieva.otus.library_hw12_security.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final MD5PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           MessageService messageService,
                           MD5PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new LibraryException(
                        messageService.getMessage("security.unknown.login", login)));
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
