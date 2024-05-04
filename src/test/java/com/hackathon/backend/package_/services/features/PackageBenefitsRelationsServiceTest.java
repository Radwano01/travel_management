package com.hackathon.backend.package_.services.features;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.services.package_.packageFeatures.PackageBenefitsRelationsService;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.hackathon.backend.utilities.package_.features.BenefitUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackageBenefitsRelationsServiceTest {

    @Mock
    private PackageUtils packageUtils;

    @Mock
    private BenefitUtils benefitUtils;

    private PackageBenefitsRelationsService packageBenefitsRelationsService;

    @BeforeEach
    void setUp() {
        packageBenefitsRelationsService = new PackageBenefitsRelationsService(
                packageUtils,
                benefitUtils
        );
    }

    @Test
    void addPackageBenefit() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageDetails(new PackageDetailsEntity());
        BenefitEntity benefitEntity = new BenefitEntity();
        benefitEntity.setId(1);

        when(packageUtils.findById(1)).thenReturn(packageEntity);
        when(benefitUtils.findById(1)).thenReturn(benefitEntity);

        //when
        ResponseEntity<?> response = packageBenefitsRelationsService.addPackageBenefit(1, 1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void removePackageBenefit() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageDetails(new PackageDetailsEntity());
        BenefitEntity benefitEntity = new BenefitEntity();
        benefitEntity.setId(1);

        packageEntity.getPackageDetails().getBenefits().add(benefitEntity);

        when(packageUtils.findById(1)).thenReturn(packageEntity);
        when(benefitUtils.findById(1)).thenReturn(benefitEntity);

        //when
        ResponseEntity<?> response = packageBenefitsRelationsService.removePackageBenefit(1, 1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}