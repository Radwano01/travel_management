package com.hackathon.backend.package_.services.features;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.services.package_.packageFeatures.BenefitService;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.features.BenefitUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BenefitServiceTest {

    @Mock
    BenefitUtils benefitUtils;

    @Mock
    PackageDetailsUtils packageDetailsUtils;

    @InjectMocks
    BenefitService benefitService;

    @Test
    void createBenefit() {
        //given
        String benefit = "testBenefit";

        //behavior
        when(benefitUtils.existsByBenefit(benefit)).thenReturn(false);

        //when
        ResponseEntity<?> response = benefitService.createBenefit(benefit);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getBenefits() {
        //given
        BenefitEntity benefit = new BenefitEntity("testBenefit");

        List<BenefitEntity> benefitEntities = new ArrayList<>();
        benefitEntities.add(benefit);

        //behavior
        when(benefitUtils.findAll()).thenReturn(benefitEntities);

        //when
        ResponseEntity<?> response = benefitService.getBenefits();

        List<BenefitEntity> responseData = (List<BenefitEntity>) response.getBody();
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(benefitEntities, responseData);
    }

    @Test
    void editBenefit() {
        //given
        int benefitId = 1;
        String benefit = "testBenefit1";

        BenefitEntity benefitEntity = new BenefitEntity("testBenefit");

        //behavior
        when(benefitUtils.findById(benefitId)).thenReturn(benefitEntity);

        //when
        ResponseEntity<?> response = benefitService.editBenefit(benefitId, benefit);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(benefit, benefitEntity.getBenefit());
    }

    @Test
    void deleteBenefit() {
        //given
        int benefitId = 1;

        BenefitEntity benefitEntity = new BenefitEntity();
        benefitEntity.setId(benefitId);
        benefitEntity.setBenefit("testRoadmap");

        PackageDetailsEntity packageDetails = new PackageDetailsEntity();
        packageDetails.setBenefits(List.of(benefitEntity));

        //behavior
        when(benefitUtils.findById(benefitId)).thenReturn(benefitEntity);

        //when
        ResponseEntity<?> response = benefitService.deleteBenefit(benefitId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(benefitUtils).delete(benefitEntity);
    }
}