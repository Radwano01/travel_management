package com.hackathon.backend.package_.services;

import com.hackathon.backend.dto.packageDto.EssentialPackageDto;
import com.hackathon.backend.dto.packageDto.PackageDetailsDto;
import com.hackathon.backend.dto.packageDto.PackageDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.services.package_.PackageService;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.PackageEvaluationUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.hackathon.backend.utilities.package_.features.BenefitUtils;
import com.hackathon.backend.utilities.package_.features.RoadmapUtils;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PackageServiceTest {

    @Mock
    CountryUtils countryUtils;

    @Mock
    PackageUtils packageUtils;

    @Mock
    PackageEvaluationUtils packageEvaluationUtils;

    @Mock
    RoadmapUtils roadmapUtils;

    @Mock
    BenefitUtils benefitUtils;

    @Mock
    PackageDetailsUtils packageDetailsUtils;

    @InjectMocks
    PackageService packageService;

    @Test
    void createPackage() {
        //given
        int countryId = 1;

        PackageDto packageDto = new PackageDto();
        packageDto.setPackageName("testPackage");
        packageDto.setPrice(100);
        packageDto.setMainImage("testImage");

        PackageDetailsDto packageDetailsDto = new PackageDetailsDto();
        packageDetailsDto.setImageOne("testImageOne");
        packageDetailsDto.setImageTwo("testImageTwo");
        packageDetailsDto.setImageThree("testImageThree");
        packageDetailsDto.setDescription("testDesc");

        packageDto.setPackageDetails(packageDetailsDto);

        CountryEntity countryEntity = new CountryEntity();


        //behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(countryEntity);

        //when
        ResponseEntity<?> response = packageService.createPackage(countryId, packageDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getPackagesByCountry() {
        //given
        int countryId = 1;

        List<EssentialPackageDto> essentialPackageDtos = new ArrayList<>();

        EssentialPackageDto essentialPackageDto = new EssentialPackageDto();
        essentialPackageDto.setPackageName("testPackage");
        essentialPackageDto.setMainImage("testImage");

        essentialPackageDtos.add(essentialPackageDto);

        //behavior
        when(packageUtils.findPackagesByCountryId(countryId)).thenReturn(essentialPackageDtos);

        //when
        ResponseEntity<?> response = packageService.getPackagesByCountry(countryId);

        List<EssentialPackageDto> responseData = (List<EssentialPackageDto>) response.getBody();
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseData);
        assertEquals(essentialPackageDtos.get(0).getPackageName(), responseData.get(0).getPackageName());
        assertEquals(essentialPackageDtos.get(0).getMainImage(), responseData.get(0).getMainImage());
    }

    @Test
    void editPackage() {
        //given
        int packageId = 1;

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);
        packageEntity.setPackageName("testPackage");
        packageEntity.setMainImage("testImage");
        packageEntity.setPrice(100);
        packageEntity.setRate(1.88f);

        PackageDto packageDto = new PackageDto();
        packageDto.setPackageName("testPackage1");
        packageDto.setMainImage("testImage1");
        packageDto.setPrice(111);
        packageDto.setRate(1.99f);

        //behavior
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);

        //when
        ResponseEntity<?> response = packageService.editPackage(packageId, packageDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(packageDto.getPackageName(), packageEntity.getPackageName());
        assertEquals(packageDto.getMainImage(), packageEntity.getMainImage());
        assertEquals(packageDto.getPrice(), packageEntity.getPrice());
        assertEquals(packageDto.getRate(), packageEntity.getRate());

    }

    @Test
    void deletePackage() {
        //given
        int packageId = 1;
        PackageEntity packageEntity = new PackageEntity();
        PackageDetailsEntity packageDetailsEntity = new PackageDetailsEntity();

        packageEntity.setPackageDetails(packageDetailsEntity);

        PackageService packageService = new PackageService(
                packageUtils, packageDetailsUtils,
                countryUtils,roadmapUtils,benefitUtils,
                packageEvaluationUtils
        );

        //behavior
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);

        //when
        ResponseEntity<?> response = packageService.deletePackage(packageId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(packageUtils).findById(packageId);
        verify(packageUtils).delete(packageEntity);
        verify(packageDetailsUtils).delete(packageDetailsEntity);
    }
}