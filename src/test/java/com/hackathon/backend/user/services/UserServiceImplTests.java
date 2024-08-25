package com.hackathon.backend.user.services;

import com.hackathon.backend.config.TwilioConfig;
import com.hackathon.backend.dto.userDto.*;
import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.hotel.HotelEvaluationRepository;
import com.hackathon.backend.repositories.hotel.RoomBookingRepository;
import com.hackathon.backend.repositories.package_.PackageBookingRepository;
import com.hackathon.backend.repositories.package_.PackageEvaluationRepository;
import com.hackathon.backend.repositories.plane.PlaneSeatsBookingRepository;
import com.hackathon.backend.repositories.user.RoleRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import com.hackathon.backend.security.JWTGenerator;
import com.hackathon.backend.services.user.impl.UserServiceImpl;
import com.hackathon.backend.utilities.S3Service;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.mail.MessagingException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTGenerator jwtGenerator;

    @Mock
    private S3Service s3Service;

    @Mock
    private TwilioConfig twilioConfig;

    @Mock
    private HotelEvaluationRepository hotelEvaluationRepository;

    @Mock
    private PackageEvaluationRepository packageEvaluationRepository;

    @Mock
    private RoomBookingRepository roomBookingRepository;

    @Mock
    private PackageBookingRepository packageBookingRepository;

    @Mock
    private PlaneSeatsBookingRepository planeSeatsBookingRepository;

    @Value("${VERIFY_LINK_TO_USER}")
    private String verifyLink;


    @Test
    public void testRegisterUser_Success() {
        MockMultipartFile mainImage =
                new MockMultipartFile(
                        "mainImage", "mainImage.jpg", "image/jpeg", "random-image-content".getBytes());

        RegisterUserDto dto = new RegisterUserDto();
        dto.setUsername("user");
        dto.setEmail("user@example.com");
        dto.setPassword("Password1!");
        dto.setFullName("User Name");
        dto.setCountry("Country");
        dto.setPhoneNumber("1234567890");
        dto.setAddress("Address");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        dto.setImage(mainImage);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRole("USER");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(s3Service.uploadFile(mainImage)).thenReturn("uploadedImageUrl");
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());
        when(roleRepository.findByRole("USER")).thenReturn(Optional.of(roleEntity));

        CompletableFuture<ResponseEntity<?>> response = userServiceImpl.registerUser(dto);

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
    }

    @Test
    public void testLoginUser_Success() {
        LoginUserDto dto = new LoginUserDto();
        dto.setUsername("user");
        dto.setPassword("Password1!");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtGenerator.generateToken(any())).thenReturn("jwtToken");
        when(userRepository.findUserByUsername("user")).thenReturn(Optional.of(new UserEntity()));

        CompletableFuture<ResponseEntity<?>> response = userServiceImpl.loginUser(dto);

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
    }

    @Test
    public void testDeleteUser_Success() {
        long userId = 1L;
        UserEntity user = new UserEntity();
        user.setImage("imageUrl");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(hotelEvaluationRepository.findHotelEvaluationByUserId(userId)).thenReturn(null);
        when(packageEvaluationRepository.findPackageEvaluationByUserId(userId)).thenReturn(null);
        when(roomBookingRepository.findByUserId(userId)).thenReturn(Collections.emptyList());
        when(packageBookingRepository.findByUserId(userId)).thenReturn(Collections.emptyList());
        when(planeSeatsBookingRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        when(s3Service.deleteFile(anyString())).thenReturn(true);

        doNothing().when(userRepository).delete(any(UserEntity.class));

        CompletableFuture<ResponseEntity<?>> response = userServiceImpl.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
    }


    @Test
    public void testEditUserPassword_Success() {
        long userId = 1L;
        EditUserDto dto = new EditUserDto();
        dto.setPassword("NewPassword1!");

        UserEntity user = new UserEntity();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        CompletableFuture<ResponseEntity<?>> response = userServiceImpl.editUserPassword(userId, dto);

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
    }

    @Test
    public void testVerifyUser_Success() {
        String email = "user@example.com";
        UserEntity user = new UserEntity();
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        CompletableFuture<ResponseEntity<?>> response = userServiceImpl.verifyUser(email);

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
    }

    @Test
    public void testSendVerificationLink_Success() throws MessagingException {
        long userId = 1L;
        String token = "verificationToken";
        UserEntity user = new UserEntity();
        user.setEmail("user@example.com");

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        CompletableFuture<ResponseEntity<?>> response = userServiceImpl.sendVerificationLink(userId, token);

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
    }
}
