package com.hackathon.backend.utilities.user;


import com.hackathon.backend.dto.userDto.EditUserDetailsDto;
import com.hackathon.backend.dto.userDto.EditUserDto;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.user.UserRepository;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;


@Component
public class UserUtils {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final JavaMailSender javaMailSender;

    @Autowired
    public UserUtils(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     S3Service s3Service,
                     JavaMailSender javaMailSender
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Service = s3Service;
        this.javaMailSender = javaMailSender;
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

    public MimeMessage prepareTheMessageEmail(String email, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(message, true);
        return mimeMessage;
    }

    public void sendMessageToEmail(MimeMessage mimeMessage){
        javaMailSender.send(mimeMessage);
    }

    public boolean checkHelper(EditUserDto editUserDto){
        return  editUserDto.getPassword() != null;
    }

    public boolean checkHelper(EditUserDetailsDto editUserDetailsDto){
        return  editUserDetailsDto.getFullName() != null ||
                editUserDetailsDto.getCountry() != null ||
                editUserDetailsDto.getPhoneNumber() != null ||
                editUserDetailsDto.getAddress() != null ||
                editUserDetailsDto.getDateOfBirth() != null ||
                editUserDetailsDto.getImage() != null;
    }

    public void editHelper(UserEntity user,
                           EditUserDto editUserDto) {
        if (editUserDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(editUserDto.getPassword()));
        }
    }

    public void editHelper(UserEntity user,
                           EditUserDetailsDto editUserDetailsDto){
        if (editUserDetailsDto.getFullName() != null) {
            user.setFullName(editUserDetailsDto.getFullName());
        }
        if (editUserDetailsDto.getCountry() != null) {
            user.setCountry(editUserDetailsDto.getCountry());
        }
        if (editUserDetailsDto.getPhoneNumber() != null) {
            user.setPhoneNumber(editUserDetailsDto.getPhoneNumber());
        }
        if (editUserDetailsDto.getAddress() != null) {
            user.setAddress(editUserDetailsDto.getAddress());
        }
        if (editUserDetailsDto.getDateOfBirth() != null) {
            user.setDateOfBirth(editUserDetailsDto.getDateOfBirth());
        }
        if (editUserDetailsDto.getImage() != null && !editUserDetailsDto.getImage().isEmpty()) {
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                s3Service.deleteFile(user.getImage());
            }
            user.setImage(editUserDetailsDto.getImage());
        }
    }
}
