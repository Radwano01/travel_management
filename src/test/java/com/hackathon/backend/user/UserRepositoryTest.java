package com.hackathon.backend.user;

import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.user.RoleRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class UserRepositoryTest {

    // User repository under test
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp(){
        RoleEntity role = new RoleEntity(
                "USER"
        );
        roleRepository.save(role);

        String email = "test@gmail.com";
        UserEntity user = new UserEntity(
                "testUser",
                email,
                false,
                "image_url",
                "testpass",
                role
        );
        userRepository.save(user);
    }

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void UserExistByEmail() {
        //given
        String email = "test@gmail.com";
        //when
        boolean response = userRepository.existsByEmail(email);
        //then
        assertTrue(response);
    }

    @Test
    void FindUserByEmail() {
        //given
        String email = "test@gmail.com";
        //when
        Optional<UserEntity> response = userRepository.findUserByEmail(email);
        assertTrue(response.isPresent());
        //then
        assertEquals(email, response.get().getEmail());
    }

    @Test
    void FindUserByUsername() {
        //given
        String username = "testUser";
        //when
        Optional<UserEntity> user = userRepository.findUserByUsername(username);
        assertTrue(user.isPresent());
        //then
        assertEquals(username, user.get().getUsername());
    }

    @Test
    void ExistsUsernameByUsername() {
        String username = "testUser";
        //when
        boolean response = userRepository.existsUsernameByUsername(username);
        //then
        assertTrue(response);
    }

    @Test
    void FindIdByUsername() {
        String username = "testUser";
        //when
        Optional<UserEntity> response = userRepository.findUserByUsername(username);
        assertTrue(response.isPresent());
        //then
        assertNotNull(response.get().getId());
    }
}