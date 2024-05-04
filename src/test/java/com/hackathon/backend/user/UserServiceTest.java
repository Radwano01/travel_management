package com.hackathon.backend.user;

import com.hackathon.backend.dto.userDto.EditUserDto;
import com.hackathon.backend.dto.userDto.LoginUserDto;
import com.hackathon.backend.dto.userDto.RegisterUserDto;
import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.user.RoleRepository;
import com.hackathon.backend.security.JWTGenerator;
import com.hackathon.backend.services.UserService;
import com.hackathon.backend.utilities.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserUtils userUtils;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTGenerator jwtGenerator;
    @Mock
    private JavaMailSender javaMailSender;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(
                authenticationManager,
                userUtils,
                roleRepository,
                javaMailSender,
                passwordEncoder,
                jwtGenerator);
    }

    @AfterEach
    void tearDown() {
        userUtils.deleteAll();
    }

    @Test
    void registerUser() {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("user");
        registerUserDto.setEmail("user@email");
        registerUserDto.setPassword("password");

        when(roleRepository.findByRole("USER"))
                .thenReturn(Optional.of(new RoleEntity("USER")));
        //when
        ResponseEntity<?> response = userService.registerUser(registerUserDto);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void checkExistsEmail(){
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("user");
        registerUserDto.setEmail("user@email");
        registerUserDto.setPassword("password");

        when(userUtils.existsByEmail(registerUserDto.getEmail())).thenReturn(true);
        //when
        ResponseEntity<?> response = userService.registerUser(registerUserDto);
        //then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void checkExistsUsername(){
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("user");
        registerUserDto.setEmail("user@email");
        registerUserDto.setPassword("password");

        when(userUtils.existsUsernameByUsername(registerUserDto.getUsername())).thenReturn(true);
        //when
        ResponseEntity<?> response = userService.registerUser(registerUserDto);
        //then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void loginUser() {
        //given
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        String token = "mockedToken";
        when(jwtGenerator.generateToken(authentication))
                .thenReturn(token);

        UserEntity user = new UserEntity();
        when(userUtils.findUserByUsername(anyString()))
                .thenReturn(user);

        when(jwtGenerator.getUsernameFromJWT(anyString()))
                .thenReturn("username");

        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setUsername("username");
        loginUserDto.setPassword("password");

        //when
        ResponseEntity<?> response = userService.loginUser(loginUserDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteUser() {
        //given
        long userId = 1L;
        //when
        ResponseEntity<?> response = userService.deleteUser(userId);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editUser() {
        //given
        long userId = 1L;
        EditUserDto editUserDto = new EditUserDto();
        editUserDto.setUsername("user");
        editUserDto.setEmail("user@email");
        editUserDto.setPassword("password");
        editUserDto.setImage("user_image_url");

        when(userUtils.findById(userId)).thenReturn(new UserEntity());
        //when
        ResponseEntity<?> response = userService.editUser(userId,editUserDto);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void verifyUser() {
        //given
        String email = "user@gmail";

        when(userUtils.findUserByEmail(email)).thenReturn(new UserEntity());
        //when
        ResponseEntity<?> response = userService.verifyUser(email);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void sendVerificationLink(){
        //given
        long userId = 1L;
        String token = "test";

        when(userUtils.findById(userId)).thenReturn(new UserEntity());

        //when
        ResponseEntity<?> response = userService.sendVerificationLink(userId,token);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}