package com.hackathon.backend.package_.services.features;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.services.package_.packageFeatures.PackageBenefitsRelationsService;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.hackathon.backend.utilities.package_.features.BenefitUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackageBenefitsRelationsServiceTest {

    @Mock
    PackageUtils packageUtils;

    @Mock
    BenefitUtils benefitUtils;

    @InjectMocks
    PackageBenefitsRelationsService packageBenefitsRelationsService;

    @Test
    void addPackageBenefit() {
        //given
        int packageId = 1;
        int benefitId = 1;

        BenefitEntity benefitEntity = new BenefitEntity();
        benefitEntity.setId(benefitId);
        benefitEntity.setBenefit("testBenefit");

        PackageDetailsEntity packageDetails = new PackageDetailsEntity();

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);
        packageEntity.setPackageDetails(packageDetails);

        //behavior
        when(benefitUtils.findById(benefitId)).thenReturn(benefitEntity);
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);

        //when
        ResponseEntity<?> response = packageBenefitsRelationsService.addPackageBenefit(packageId,benefitId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(benefitUtils).findById(benefitId);
        verify(packageUtils).findById(packageId);
        verify(benefitUtils).save(benefitEntity);
        verify(packageUtils).save(packageEntity);
    }

    @Test
    void removePackageBenefit() {
        //given
        int packageId = 1;
        int benefitId = 1;

        BenefitEntity benefitEntity = new BenefitEntity();
        benefitEntity.setId(benefitId);
        benefitEntity.setBenefit("testBenefit");

        PackageDetailsEntity packageDetails = new PackageDetailsEntity();
        packageDetails.getBenefits().add(benefitEntity);

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);
        packageEntity.setPackageDetails(packageDetails);

        //behavior
        when(benefitUtils.findById(benefitId)).thenReturn(benefitEntity);
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);

        //when
        ResponseEntity<?> response = packageBenefitsRelationsService.removePackageBenefit(packageId,benefitId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(benefitUtils).findById(benefitId);
        verify(packageUtils).findById(packageId);
        verify(benefitUtils).save(benefitEntity);
        verify(packageUtils).save(packageEntity);
    }
}