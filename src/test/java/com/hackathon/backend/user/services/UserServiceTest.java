package com.hackathon.backend.user.services;

import com.hackathon.backend.dto.userDto.AuthResponseDto;
import com.hackathon.backend.dto.userDto.EditUserDto;
import com.hackathon.backend.dto.userDto.LoginUserDto;
import com.hackathon.backend.dto.userDto.RegisterUserDto;
import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.user.RoleRepository;
import com.hackathon.backend.security.JWTGenerator;
import com.hackathon.backend.services.UserService;
import com.hackathon.backend.utilities.UserUtils;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserUtils userUtils;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JWTGenerator jwtGenerator;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    S3Service s3Service;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser() {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto("username", "email@example.com", "Password123!");
        RoleEntity role = new RoleEntity();

        //behavior
        when(userUtils.existsByEmail(registerUserDto.getEmail())).thenReturn(false);
        when(userUtils.existsUsernameByUsername(registerUserDto.getUsername())).thenReturn(false);
        when(roleRepository.findByRole("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("encodedPassword");

        //when
        ResponseEntity<?> response = userService.registerUser(registerUserDto);

        assertEquals("Account Created", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(userUtils, times(1)).save(any(UserEntity.class));
    }

    @Test
    void loginUser() {
        //given
        LoginUserDto loginUserDto = new LoginUserDto("username", "password");
        Authentication authentication = mock(Authentication.class);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setId(1L);
        userEntity.setImage("image_url");

        //behavior
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtGenerator.generateToken(authentication)).thenReturn("token");
        when(jwtGenerator.getUsernameFromJWT("token")).thenReturn("username");
        when(userUtils.findUserByUsername("username")).thenReturn(userEntity);

        //when
        ResponseEntity<?> response = userService.loginUser(loginUserDto);

        //then
        AuthResponseDto authResponseDto = (AuthResponseDto) response.getBody();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("token", authResponseDto.getAccessToken());
        assertEquals("username", authResponseDto.getEssentialUserDto().getUsername());
        assertEquals(1L, authResponseDto.getEssentialUserDto().getId());
        assertEquals("image_url", authResponseDto.getEssentialUserDto().getImage());
    }

    @Test
    void deleteUser() {
        //given
        long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setImage("image_url");

        //behavior
        when(userUtils.findById(userId)).thenReturn(userEntity);

        //when
        ResponseEntity<?> response = userService.deleteUser(userId);

        //then
        assertEquals("Account deleted successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(s3Service, times(1)).deleteFile("image_url");
        verify(userUtils, times(1)).delete(userEntity);
    }

    @Test
    void editUser() {
        //given
        long userId = 1L;

        EditUserDto editUserDto = new EditUserDto(
                "Password123!",
                new MockMultipartFile("newImage1", "image.jpg", "image/jpeg", "image data".getBytes())
        );

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setPassword("Password12345!");
        userEntity.setImage("oldImage");

        //behavior
        when(userUtils.findById(userId)).thenReturn(userEntity);
        when(passwordEncoder.encode(editUserDto.getPassword())).thenReturn("encodedNewPassword123!!");
        when(s3Service.uploadFile(any(MockMultipartFile.class))).thenReturn("newImageUrl");

        //when
        ResponseEntity<?> response = userService.editUser(userId, editUserDto);

        //then
        assertEquals("user updated successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("encodedNewPassword123!!", userEntity.getPassword());
        assertEquals("newImageUrl", userEntity.getImage());

        verify(s3Service, times(1)).deleteFile("oldImage");
        verify(s3Service, times(1)).uploadFile(editUserDto.getImage());
        verify(userUtils, times(1)).save(userEntity);
    }




    @Test
    void verifyUser() {
        //given
        String email = "user@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setVerificationStatus(false);

        //behavior
        when(userUtils.findUserByEmail(email)).thenReturn(userEntity);

        //when
        ResponseEntity<?> response = userService.verifyUser(email);

        //then
        assertEquals("User has been verified", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(userEntity.isVerificationStatus());

        verify(userUtils, times(1)).findUserByEmail(email);
        verify(userUtils, times(1)).save(userEntity);
    }
}