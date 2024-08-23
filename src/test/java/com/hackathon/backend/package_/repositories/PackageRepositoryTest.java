package com.hackathon.backend.package_.repositories;

import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.dto.packageDto.PackageEvaluationDto;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PackageRepositoryTest {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    private int packageId;

    @BeforeEach
    void setUp() {
        // Create and save a user
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setImage("testUserImage.jpg");
        userRepository.save(user);

        // Create and save a package
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("package name");
        packageRepository.save(packageEntity);

        // Save the package ID for use in the test
        packageId = packageEntity.getId();

        // Create and save a package evaluation
        PackageEvaluationEntity packageEvaluation = new PackageEvaluationEntity(
                "Excellent package!",
                5,
                user,
                packageEntity
        );
        packageEntity.getPackageEvaluations().add(packageEvaluation);

        // Save the package evaluation
        packageRepository.save(packageEntity);
    }

    @AfterEach
    void tearDown() {
        packageRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void itShouldReturnAllEvaluationsByPackageId() {
        //given
        int expectedPackageId = packageId;

        //when
        List<PackageEvaluationDto> evaluations = packageRepository.findAllEvaluationsPackageByPackageId(expectedPackageId);

        //then
        assertNotNull(evaluations);
        assertEquals(1, evaluations.size());
        PackageEvaluationDto evaluation = evaluations.get(0);
        assertEquals("Excellent package!", evaluation.getComment());
        assertEquals(5, evaluation.getRate());
        assertEquals("testUser", evaluation.getUsername());
        assertEquals("testUserImage.jpg", evaluation.getUserImage());
    }
}
