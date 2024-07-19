package com.hackathon.backend.user.services;

import com.hackathon.backend.dto.userDto.*;
import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.hotel.RoomBookingRepository;
import com.hackathon.backend.repositories.package_.PackageBookingRepository;
import com.hackathon.backend.repositories.plane.PlaneSeatsBookingRepository;
import com.hackathon.backend.repositories.user.RoleRepository;
import com.hackathon.backend.security.JWTGenerator;
import com.hackathon.backend.services.user.UserService;
import com.hackathon.backend.utilities.user.UserUtils;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    @Mock
    RoomBookingRepository roomBookingRepository;

    @Mock
    PackageBookingRepository packageBookingRepository;

    @Mock
    PlaneSeatsBookingRepository planeSeatsBookingRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser() throws ExecutionException, InterruptedException {
        //given
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("testemail");
        registerUserDto.setUsername("testusername");
        registerUserDto.setPassword("Testpassword1!");
        registerUserDto.setImage("testimage");
        registerUserDto.setFullName("testname");
        registerUserDto.setCountry("testcountry");
        registerUserDto.setPhoneNumber("testphonenumber");
        registerUserDto.setAddress("testaddress");
        registerUserDto.setDateOfBirth(LocalDate.now());
        RoleEntity role = new RoleEntity();

        //behavior
        when(userUtils.existsByEmail(registerUserDto.getEmail())).thenReturn(false);
        when(userUtils.existsUsernameByUsername(registerUserDto.getUsername())).thenReturn(false);
        when(roleRepository.findByRole("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("encodedPassword");

        //when
        CompletableFuture<ResponseEntity<?>> response = userService.registerUser(registerUserDto);

        assertEquals("Account Created", response.get().getBody());
        assertEquals(HttpStatus.OK, response.get().getStatusCode());
        verify(userUtils, times(1)).save(any(UserEntity.class));
    }

    @Test
    void loginUser() throws ExecutionException, InterruptedException {
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
        CompletableFuture<ResponseEntity<?>> response = userService.loginUser(loginUserDto);

        //then
        AuthResponseDto authResponseDto = (AuthResponseDto) response.get().getBody();
        assertEquals(HttpStatus.OK, response.get().getStatusCode());
        assertEquals("token", authResponseDto.getAccessToken());
        assertEquals("username", authResponseDto.getEssentialUserDto().getUsername());
        assertEquals(1L, authResponseDto.getEssentialUserDto().getId());
        assertEquals("image_url", authResponseDto.getEssentialUserDto().getImage());
    }

    @Test
    void deleteUser() throws ExecutionException, InterruptedException {
        //given
        long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setImage("image_url");

        // Mock behavior
        when(userUtils.findById(userId)).thenReturn(userEntity);
        when(roomBookingRepository.findByUserId(userId)).thenReturn(Collections.emptyList());
        when(packageBookingRepository.findByUserId(userId)).thenReturn(Collections.emptyList());
        when(planeSeatsBookingRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        //when
        CompletableFuture<ResponseEntity<?>> response = userService.deleteUser(userId);

        //then
        assertEquals("Account deleted successfully", response.get().getBody());
        assertEquals(HttpStatus.OK, response.get().getStatusCode());
        verify(s3Service, times(1)).deleteFile("image_url");
        verify(userUtils, times(1)).delete(userEntity);
    }

    @Test
    void editUser() throws ExecutionException, InterruptedException {
        // given
        long userId = 1L;

        EditUserDto editUserDto = new EditUserDto(
                "Password123!"
        );

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setPassword("Password12345!");

        // Mock behavior
        when(userUtils.findById(userId)).thenReturn(userEntity);
        when(userUtils.checkHelper(editUserDto)).thenReturn(true);
        when(passwordEncoder.encode(editUserDto.getPassword())).thenReturn("encodedNewPassword123!!");

        doAnswer(invocation -> {
            UserEntity entity = invocation.getArgument(0);
            EditUserDto dto = invocation.getArgument(1);

            entity.setPassword(passwordEncoder.encode(dto.getPassword()));

            return null;
        }).when(userUtils).editHelper(any(UserEntity.class), any(EditUserDto.class));

        // when
        CompletableFuture<ResponseEntity<?>> response = userService.editUser(userId, editUserDto);

        // then
        assertEquals("user updated successfully", response.get().getBody());
        assertEquals(HttpStatus.OK, response.get().getStatusCode());
        assertEquals("encodedNewPassword123!!", userEntity.getPassword());
        verify(userUtils, times(1)).save(userEntity);
    }




    @Test
    void verifyUser() throws ExecutionException, InterruptedException {
        //given
        String email = "user@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setVerificationStatus(false);

        //behavior
        when(userUtils.findUserByEmail(email)).thenReturn(userEntity);

        //when
        CompletableFuture<ResponseEntity<?>> response = userService.verifyUser(email);

        //then
        assertEquals("User has been verified", response.get().getBody());
        assertEquals(HttpStatus.OK, response.get().getStatusCode());
        assertTrue(userEntity.isVerificationStatus());

        verify(userUtils, times(1)).findUserByEmail(email);
        verify(userUtils, times(1)).save(userEntity);
    }

    @Test
    void testGetUserDetails() throws ExecutionException, InterruptedException {
        // given
        long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setEmail("test@example.com");
        userEntity.setImage("test-image.jpg");
        userEntity.setVerificationStatus(true);
        userEntity.setFullName("Test User");
        userEntity.setCountry("Testland");
        userEntity.setPhoneNumber("123456789");
        userEntity.setAddress("123 Test St");
        userEntity.setDateOfBirth(LocalDate.of(2000, 1, 1));

        //behavior
        when(userUtils.findById(userId)).thenReturn(userEntity);

        // when
        CompletableFuture<ResponseEntity<?>> responseFuture = userService.getUserDetails(userId);
        ResponseEntity<?> responseEntity = responseFuture.get();

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserDto returnedUserDto = (UserDto) responseEntity.getBody();

        assertNotNull(returnedUserDto);
        assertEquals(userEntity.getUsername(), returnedUserDto.getUsername());
        assertEquals(userEntity.getEmail(), returnedUserDto.getEmail());
        assertEquals(userEntity.getImage(), returnedUserDto.getImage());
        assertEquals(userEntity.isVerificationStatus(), returnedUserDto.isVerificationStatus());
        assertEquals(userEntity.getFullName(), returnedUserDto.getFullName());
        assertEquals(userEntity.getCountry(), returnedUserDto.getCountry());
        assertEquals(userEntity.getPhoneNumber(), returnedUserDto.getPhoneNumber());
        assertEquals(userEntity.getAddress(), returnedUserDto.getAddress());
        assertEquals(userEntity.getDateOfBirth(), returnedUserDto.getDateOfBirth());

        verify(userUtils, times(1)).findById(userId);
    }

    @Test
    void editUserDetails() throws ExecutionException, InterruptedException {
        // given
        long userId = 1L;
        EditUserDetailsDto editUserDetailsDto = new EditUserDetailsDto();

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        //behavior
        when(userUtils.findById(userId)).thenReturn(userEntity);
        when(userUtils.checkHelper(editUserDetailsDto)).thenReturn(true);

        // when
        CompletableFuture<ResponseEntity<?>> response = userService.editUserDetails(userId, editUserDetailsDto);

        // then
        assertEquals(HttpStatus.OK, response.get().getStatusCode());
        assertEquals("User Details edited successfully", response.get().getBody());
        verify(userUtils, times(1)).findById(userId);
        verify(userUtils, times(1)).editHelper(userEntity, editUserDetailsDto);
        verify(userUtils, times(1)).save(userEntity);
    }
}