package com.hackathon.backend.package_.services;

import com.hackathon.backend.dto.packageDto.EditPackageDto;
import com.hackathon.backend.dto.packageDto.PostPackageDto;
import com.hackathon.backend.dto.packageDto.GetEssentialPackageDto;
import com.hackathon.backend.dto.packageDto.GetPackageDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.services.package_.PackageService;
import com.hackathon.backend.utilities.amazonServices.S3Service;
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
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    S3Service s3Service;

    @InjectMocks
    PackageService packageService;

    @Test
    void createPackage() {
        // given
        int countryId = 1;

        PostPackageDto p = new PostPackageDto(
                "testPackage",
                100.0f,
                0,
                new MockMultipartFile("mainImage", "mainImage.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageOne", "imageOne.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageTwo", "imageTwo.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageThree", "imageThree.jpg", "image/jpeg", new byte[0]),
                "testDesc"
        );

        //behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(new CountryEntity());

        // when
        ResponseEntity<?> response = packageService.createPackage(countryId, p);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getPackagesByCountry() {
        //given
        int countryId = 1;

        List<GetEssentialPackageDto> getEssentialPackageDtos = new ArrayList<>();

        GetEssentialPackageDto getEssentialPackageDto = new GetEssentialPackageDto();
        getEssentialPackageDto.setPackageName("testPackage");
        getEssentialPackageDto.setMainImage("testImage");

        getEssentialPackageDtos.add(getEssentialPackageDto);

        //behavior
        when(packageUtils.findPackagesByCountryId(countryId)).thenReturn(getEssentialPackageDtos);

        //when
        ResponseEntity<?> response = packageService.getPackagesByCountry(countryId);

        List<GetEssentialPackageDto> responseData = (List<GetEssentialPackageDto>) response.getBody();
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseData);
        assertEquals(getEssentialPackageDtos.get(0).getPackageName(), responseData.get(0).getPackageName());
        assertEquals(getEssentialPackageDtos.get(0).getMainImage(), responseData.get(0).getMainImage());
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

        EditPackageDto editPackageDto = new EditPackageDto(
                "testPackage1",
                100,
                1.1f,
                new MockMultipartFile("mainImage", "mainImage.jpg", "image/jpeg", new byte[0])

        );

        //behavior
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);
        when(s3Service.uploadFile(editPackageDto.getMainImage())).thenReturn("mainImage");

        //when
        ResponseEntity<?> response = packageService.editPackage(packageId, editPackageDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testPackage1", packageEntity.getPackageName());
        assertEquals("mainImage", packageEntity.getMainImage());
        assertEquals(100, packageEntity.getPrice());
        assertEquals(1.1f, packageEntity.getRate());

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
                packageEvaluationUtils,s3Service
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