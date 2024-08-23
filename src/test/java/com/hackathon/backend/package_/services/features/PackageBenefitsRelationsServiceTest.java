package com.hackathon.backend.package_.services.features;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.BenefitRepository;
import com.hackathon.backend.services.package_.packageFeatures.PackageBenefitsRelationsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PackageBenefitsRelationsServiceTest {

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private BenefitRepository benefitRepository;

    @InjectMocks
    private PackageBenefitsRelationsService packageBenefitsRelationsService;

    @Test
    void addPackageBenefit_ShouldReturnOk_WhenBenefitIsAdded() {
        // given
        int packageId = 1;
        int benefitId = 1;

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageDetails(new PackageDetailsEntity());
        BenefitEntity benefitEntity = new BenefitEntity("Benefit 1");
        benefitEntity.setId(benefitId);

        // behavior
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));
        when(benefitRepository.findById(benefitId)).thenReturn(Optional.of(benefitEntity));

        // when
        ResponseEntity<String> response = packageBenefitsRelationsService.addPackageBenefit(packageId, benefitId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Benefit added successfully", response.getBody());
        assertTrue(packageEntity.getPackageDetails().getBenefits().contains(benefitEntity));
    }

    @Test
    void addPackageBenefit_ShouldReturnAlreadyValidException_WhenBenefitAlreadyExists() {
        // given
        int packageId = 1;
        int benefitId = 1;

        PackageEntity packageEntity = new PackageEntity();
        PackageDetailsEntity packageDetailsEntity = new PackageDetailsEntity();
        packageEntity.setPackageDetails(packageDetailsEntity);

        BenefitEntity benefitEntity = new BenefitEntity("Benefit 1");
        benefitEntity.setId(benefitId);
        packageDetailsEntity.getBenefits().add(benefitEntity);

        // behavior
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));
        when(benefitRepository.findById(benefitId)).thenReturn(Optional.of(benefitEntity));

        // when
        ResponseEntity<String> response = packageBenefitsRelationsService.addPackageBenefit(packageId, benefitId);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("This benefit is already existed", response.getBody());
    }

    @Test
    void removePackageBenefit_ShouldReturnOk_WhenBenefitIsRemoved() {
        // given
        int packageId = 1;
        int benefitId = 1;

        PackageEntity packageEntity = new PackageEntity();
        PackageDetailsEntity packageDetailsEntity = new PackageDetailsEntity();
        packageEntity.setPackageDetails(packageDetailsEntity);

        BenefitEntity benefitEntity = new BenefitEntity("Benefit 1");
        benefitEntity.setId(benefitId);
        packageDetailsEntity.getBenefits().add(benefitEntity);

        // behavior
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));
        when(benefitRepository.findById(benefitId)).thenReturn(Optional.of(benefitEntity));

        // when
        ResponseEntity<String> response = packageBenefitsRelationsService.removePackageBenefit(packageId, benefitId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Benefit removed from this package", response.getBody());
        assertFalse(packageEntity.getPackageDetails().getBenefits().contains(benefitEntity));
    }
}