package com.hackathon.backend.user.repositories;

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

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp(){
        RoleEntity role = new RoleEntity();
        role.setRole("USER");
        roleRepository.save(role);

        UserEntity user = new UserEntity(
                "testUsername",
                "test@gmail.com",
                "testPassword",
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
    void existsByEmail() {
        //given
        String email = "test@gmail.com";

        //when
        boolean response = userRepository.existsByEmail(email);

        //then
        assertTrue(response);
    }

    @Test
    void findUserByEmail() {
        //given
        String email = "test@gmail.com";

        //when
        Optional<UserEntity> response = userRepository.findUserByEmail(email);

        //then
        assertTrue(response.isPresent());
        assertEquals(response.get().getEmail(), email);
    }

    @Test
    void findUserByUsername() {
        //given
        String username = "testUsername";

        //when
        Optional<UserEntity> response = userRepository.findUserByUsername(username);

        //then
        assertTrue(response.isPresent());
        assertEquals(response.get().getUsername(), username);
    }

    @Test
    void existsUsernameByUsername() {
        //given
        String username = "testUsername";

        //when
        boolean response = userRepository.existsUsernameByUsername(username);

        //then
        assertTrue(response);
    }
}