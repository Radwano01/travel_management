package com.hackathon.backend.package_.repositories;

import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.package_.PackageEvaluationRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PackageEvaluationRepositoryTest {

    @Autowired
    private PackageEvaluationRepository packageEvaluationRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        // Create and save a user
        testUser = new UserEntity();
        testUser.setUsername("testUser");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        testUser.setFullName("Test User");
        testUser.setCountry("Test Country");
        testUser.setPhoneNumber("1234567890");
        testUser.setAddress("Test Address");
        testUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
        userRepository.save(testUser);

        // Create and save a package evaluation
        PackageEvaluationEntity testEvaluation = new PackageEvaluationEntity();
        testEvaluation.setUser(testUser);
        testEvaluation.setComment("Great package!");
        packageEvaluationRepository.save(testEvaluation);
    }

    @AfterEach
    void tearDown() {
        packageEvaluationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void itShouldReturnPackageEvaluationByUserId() {
        // When
        PackageEvaluationEntity response = packageEvaluationRepository.findPackageEvaluationByUserId(testUser.getId());

        // Then
        assertNotNull(response);
        assertEquals(testUser.getId(), response.getUser().getId());
        assertEquals("Great package!", response.getComment());
    }

    @Test
    void itShouldReturnNullWhenPackageEvaluationDoesNotExist() {
        // When
        PackageEvaluationEntity response = packageEvaluationRepository.findPackageEvaluationByUserId(999L);

        // Then
        assertNull(response);
    }
}
