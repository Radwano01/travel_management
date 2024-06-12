package com.hackathon.backend.utilities;


import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;


@Component
public class UserUtils {

    private final UserRepository userRepository;

    @Autowired
    public UserUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User id not found"));
    }

    public void save(UserEntity user) {
        userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsUsernameByUsername(String username) {
        return userRepository.existsUsernameByUsername(username);
    }

    public UserEntity findUserByUsername(String usernameFromJWT) {
        return userRepository.findUserByUsername(usernameFromJWT)
                .orElseThrow(()-> new EntityNotFoundException("username not found"));
    }

    public void deleteById(long userId) {
        userRepository.deleteById(userId);
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("Email not found"));
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void delete(UserEntity user) {
        userRepository.delete(user);
    }

    public SimpleMailMessage sendMessageToEmail(String email, String message){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Email Verification From Hackathon Project");
        mailMessage.setText("Please Click to the link to verify your account: "+message);
        return mailMessage;
    }

    public boolean isStrongPassword(String password){

        boolean isUppercase = false, isLowercase = false, isDigit = false, isSpecial = false;

        for(char c:password.toCharArray()){
            if(Character.isUpperCase(c)) isUppercase = true;
            else if(Character.isLowerCase(c)) isLowercase = true;
            else if(Character.isDigit(c)) isDigit = true;
            else isSpecial = true;
        }

        return isUppercase && isLowercase && isDigit && isSpecial;
    }

}
