package com.hackathon.backend.package_.services.features;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.services.package_.packageFeatures.BenefitService;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.features.BenefitUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BenefitServiceTest {

    @Mock
    private BenefitUtils benefitUtils;
    @Mock
    private PackageDetailsUtils packageDetailsUtils;

    private BenefitService benefitService;

    @BeforeEach
    void setUp() {
        benefitService = new BenefitService(
                benefitUtils,
                packageDetailsUtils
        );
    }

    @Test
    void createBenefit() {
        //given
        when(benefitUtils.existsByBenefit("New Benefit")).thenReturn(false);

        //when
        ResponseEntity<?> response = benefitService.createBenefit("New Benefit");

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getBenefits() {
        //given
        List<BenefitEntity> benefits = new ArrayList<>();
        benefits.add(new BenefitEntity("Benefit 1"));
        benefits.add(new BenefitEntity("Benefit 2"));

        when(benefitUtils.findAll()).thenReturn(benefits);
        //when
        ResponseEntity<?> response = benefitService.getBenefits();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editBenefit() {
        //given
        BenefitEntity existingBenefit = new BenefitEntity("Existing Benefit");

        when(benefitUtils.findById(1)).thenReturn(existingBenefit);
        //when
        ResponseEntity<?> response = benefitService
                .editBenefit(1, "Edited Benefit");

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteBenefit() {
        //given
        BenefitEntity existingBenefit = new BenefitEntity("Existing Benefit");

        List<PackageDetailsEntity> packageDetailsList = new ArrayList<>();
        PackageDetailsEntity packageDetailsEntity = new PackageDetailsEntity();
        packageDetailsEntity.setBenefits(new ArrayList<>());
        packageDetailsList.add(packageDetailsEntity);

        when(benefitUtils.findById(1)).thenReturn(existingBenefit);
        when(packageDetailsUtils.findAll()).thenReturn(packageDetailsList);
        //when
        ResponseEntity<?> response = benefitService.deleteBenefit(1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}