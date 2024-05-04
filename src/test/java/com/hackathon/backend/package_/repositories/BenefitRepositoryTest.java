package com.hackathon.backend.package_.repositories;

import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.repositories.package_.packageFeatures.BenefitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BenefitRepositoryTest {

    @Autowired
    private BenefitRepository benefitRepository;

    @Test
    void testExistsByBenefit() {
        //given
        BenefitEntity benefit = new BenefitEntity();
        benefit.setBenefit("Free Wi-Fi");
        benefitRepository.save(benefit);

        //when
        boolean exists = benefitRepository.existsByBenefit("Free Wi-Fi");
        //then
        assertTrue(exists);
    }
}