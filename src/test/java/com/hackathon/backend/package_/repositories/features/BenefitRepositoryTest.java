package com.hackathon.backend.package_.repositories.features;

import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.repositories.package_.packageFeatures.BenefitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BenefitRepositoryTest {

    @Autowired
    BenefitRepository benefitRepository;

    @BeforeEach
    void setUp(){
        BenefitEntity benefit = new BenefitEntity(
                "testFeature"
        );
        benefitRepository.save(benefit);
    }

    @AfterEach
    void tearDown(){
        benefitRepository.deleteAll();
    }

    @Test
    void existsByBenefit() {
        //given
        String benefit = "testFeature";

        //when
        boolean response = benefitRepository.existsByBenefit(benefit);

        //then
        assertTrue(response);

        benefitRepository.deleteAll();
    }
}