package com.hackathon.backend.utilities;


import com.hackathon.backend.dto.userDto.EditUserDto;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.user.UserRepository;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;


@Component
public class UserUtils {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    @Autowired
    public UserUtils(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     S3Service s3Service) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Service = s3Service;
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

    public boolean checkHelper(EditUserDto editUserDto){
        return  editUserDto.getImage() != null ||
                editUserDto.getPassword() != null;
    }

    public void editHelper(UserEntity user,
                            EditUserDto editUserDto) {
        if (editUserDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(editUserDto.getPassword()));
        }
        if(editUserDto.getImage() != null){
            if(user.getImage() != null) {
                s3Service.deleteFile(user.getImage());
            }
            String userImageName = s3Service.uploadFile(editUserDto.getImage());
            user.setImage(userImageName);
        }
    }
}
