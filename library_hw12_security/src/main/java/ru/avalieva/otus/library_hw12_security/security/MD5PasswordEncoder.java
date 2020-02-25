package ru.avalieva.otus.library_hw12_security.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.avalieva.otus.library_hw12_security.service.MessageService;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class MD5PasswordEncoder implements PasswordEncoder {

    private final MessageService messageService;

    public MD5PasswordEncoder(MessageService messageService) {
        this.messageService = messageService;
    }
    @Override
    public String encode(CharSequence charSequence) {
        return getMd5(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return getMd5(charSequence.toString()).equals(s);
    }

    private String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new SecurityException(
                    messageService.getMessage("security.no.algorithm", e.getMessage()), e);
        }
    }
}


