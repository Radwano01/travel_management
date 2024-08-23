package com.hackathon.backend.user.repositories;

import com.hackathon.backend.dto.userDto.UserDto;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    UserEntity testUser;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        testUser = new UserEntity();
        testUser.setEmail("test@example.com");
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setImage("http://example.com/image.png");
        testUser.setVerificationStatus(true);
        testUser.setFullName("Test User");
        testUser.setCountry("Country");
        testUser.setPhoneNumber("1234567890");
        testUser.setAddress("123 Test Street");
        testUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        // Cleanup after each test
        userRepository.deleteAll();
    }

    @Test
    void itShouldReturnTrueWhenEmailExists() {
        assertTrue(userRepository.existsByEmail("test@example.com"));
    }

    @Test
    void itShouldReturnFalseWhenEmailDoesNotExist() {
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }

    @Test
    void itShouldReturnUserWhenEmailExists() {
        Optional<UserEntity> user = userRepository.findUserByEmail("test@example.com");
        assertTrue(user.isPresent());
        assertEquals("testuser", user.get().getUsername());
    }

    @Test
    void itShouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<UserEntity> nonExistentUser = userRepository.findUserByEmail("nonexistent@example.com");
        assertFalse(nonExistentUser.isPresent());
    }

    @Test
    void itShouldReturnUserWhenUsernameExists() {
        Optional<UserEntity> user = userRepository.findUserByUsername("testuser");
        assertTrue(user.isPresent());
        assertEquals("test@example.com", user.get().getEmail());
    }

    @Test
    void itShouldReturnEmptyWhenUsernameDoesNotExist() {
        Optional<UserEntity> nonExistentUser = userRepository.findUserByUsername("nonexistentuser");
        assertFalse(nonExistentUser.isPresent());
    }

    @Test
    void itShouldReturnTrueWhenUsernameExists() {
        assertTrue(userRepository.existsUsernameByUsername("testuser"));
    }

    @Test
    void itShouldReturnFalseWhenUsernameDoesNotExist() {
        assertFalse(userRepository.existsUsernameByUsername("nonexistentuser"));
    }

    @Test
    void itShouldReturnUserDetailsById() {
        UserDto userDto = userRepository.findUserDetailsById(testUser.getId());
        assertNotNull(userDto);
        assertEquals(testUser.getUsername(), userDto.getUsername());
        assertEquals(testUser.getEmail(), userDto.getEmail());
        assertEquals(testUser.getImage(), userDto.getImage());
        assertEquals(testUser.getFullName(), userDto.getFullName());
        assertEquals(testUser.getCountry(), userDto.getCountry());
        assertEquals(testUser.getPhoneNumber(), userDto.getPhoneNumber());
        assertEquals(testUser.getAddress(), userDto.getAddress());
        assertEquals(testUser.getDateOfBirth(), userDto.getDateOfBirth());
    }
}
