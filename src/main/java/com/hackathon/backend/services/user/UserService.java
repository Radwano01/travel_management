package com.hackathon.backend.services.user;

import com.hackathon.backend.dto.userDto.*;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<ResponseEntity<?>> registerUser(@NonNull RegisterUserDto registerUserDto);

    CompletableFuture<ResponseEntity<?>> loginUser(LoginUserDto loginUserDto);

    CompletableFuture<ResponseEntity<?>> deleteUser(long userId);

    CompletableFuture<ResponseEntity<?>> getUserIdByEmail(String email);

    CompletableFuture<ResponseEntity<?>> editUserPassword(long userId, EditUserDto editUserDto);

    CompletableFuture<ResponseEntity<?>> verifyUser(String email);

    CompletableFuture<ResponseEntity<?>> sendVerificationLink(long userId, String token) throws MessagingException;

    CompletableFuture<ResponseEntity<?>> getUserDetails(long userId);

    CompletableFuture<ResponseEntity<?>> editUserDetails(long userId, EditUserDetailsDto editUserDetailsDto);

    void sendSms(String phoneNumber);

    boolean verifyCode(String phoneNumber, String code);


}
