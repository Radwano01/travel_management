package com.hackathon.backend.package_.repositories.features;

import com.hackathon.backend.dto.packageDto.features.GetBenefitDto;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.repositories.package_.packageFeatures.BenefitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BenefitRepositoryTest {

    @Autowired
    BenefitRepository benefitRepository;

    @BeforeEach
    void setUp() {
        // Create and save a benefit
        BenefitEntity benefit = new BenefitEntity();
        benefit.setBenefit("Free Wi-Fi");
        benefitRepository.save(benefit);
    }

    @AfterEach
    void tearDown() {
        benefitRepository.deleteAll();
    }

    @Test
    void itShouldReturnExistBenefitByBenefit() {
        //given
        String benefitName = "Free Wi-Fi";

        //when
        boolean exists = benefitRepository.existsBenefitByBenefit(benefitName);

        //then
        assertTrue(exists, "Benefit should exist in the repository");
    }

    @Test
    void itShouldReturnNotFoundBenefitByBenefit() {
        //given
        String benefitName = "Nonexistent Benefit";

        //when
        boolean response = benefitRepository.existsBenefitByBenefit(benefitName);

        //then
        assertFalse(response);
    }

    @Test
    void itShouldReturnAllBenefits() {
        //when
        List<GetBenefitDto> response = benefitRepository.findAllBenefits();

        //then
        assertNotNull(response);
        assertFalse(response.isEmpty());
        GetBenefitDto benefitDto = response.get(0);
        assertEquals("Free Wi-Fi", benefitDto.getBenefit());
    }
}
