package com.hackathon.backend.user.repositories;

import com.hackathon.backend.entities.user.RoleEntity;
import com.hackathon.backend.repositories.user.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        // Create and save roles
        RoleEntity adminRole = new RoleEntity();
        adminRole.setRole("ADMIN");
        roleRepository.save(adminRole);

        RoleEntity userRole = new RoleEntity();
        userRole.setRole("USER");
        roleRepository.save(userRole);
    }

    @AfterEach
    void tearDown() {
        // Cleanup after each test
        roleRepository.deleteAll();
    }

    @Test
    void itShouldReturnRoleWhenRoleExists() {
        Optional<RoleEntity> role = roleRepository.findByRole("ADMIN");
        assertTrue(role.isPresent());
        assertEquals("ADMIN", role.get().getRole());
    }

    @Test
    void itShouldReturnEmptyWhenRoleDoesNotExist() {
        Optional<RoleEntity> role = roleRepository.findByRole("GUEST");
        assertFalse(role.isPresent());
    }

    @Test
    void itShouldReturnTrueWhenRoleExists() {
        assertTrue(roleRepository.existsByRole("USER"));
    }

    @Test
    void itShouldReturnFalseWhenRoleDoesNotExist() {
        assertFalse(roleRepository.existsByRole("GUEST"));
    }
}
