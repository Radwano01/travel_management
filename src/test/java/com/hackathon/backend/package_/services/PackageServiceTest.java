package com.hackathon.backend.package_.services;

import com.hackathon.backend.dto.packageDto.EssentialPackageDto;
import com.hackathon.backend.dto.packageDto.PackageDetailsDto;
import com.hackathon.backend.dto.packageDto.PackageDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.services.package_.PackageService;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.PackageEvaluationUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.hackathon.backend.utilities.package_.features.BenefitUtils;
import com.hackathon.backend.utilities.package_.features.RoadmapUtils;
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
class PackageServiceTest {

    @Mock
    private PackageUtils packageUtils;

    @Mock
    private PackageDetailsUtils packageDetailsUtils;

    @Mock
    private CountryUtils countryUtils;

    @Mock
    private RoadmapUtils roadmapUtils;

    @Mock
    private BenefitUtils benefitUtils;

    @Mock
    private PackageEvaluationUtils packageEvaluationUtils;

    private PackageService packageService;

    @BeforeEach
    void setUp(){
        packageService = new PackageService(
                packageUtils,
                packageDetailsUtils,
                countryUtils,
                roadmapUtils,
                benefitUtils,
                packageEvaluationUtils
        );
    }

    @Test
    void createPackage() {
        //given
        int countryId = 1;
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setId(countryId);
        when(countryUtils.findCountryById(countryId)).thenReturn(countryEntity);

        PackageDto packageDto = new PackageDto();
        packageDto.setPackageDetails(new PackageDetailsDto());

        //when
        ResponseEntity<?> response = packageService.createPackage(countryId, packageDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getPackagesByCountry() {
        //given
        List<EssentialPackageDto> packages = new ArrayList<>();
        packages.add(new EssentialPackageDto());

        when(packageUtils.findPackagesByCountryId(1)).thenReturn(packages);
        //when
        ResponseEntity<?> response = packageService.getPackagesByCountry(1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editPackage() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        when(packageUtils.findById(1)).thenReturn(packageEntity);

        //when
        ResponseEntity<?> response = packageService.editPackage(1, new PackageDto());

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deletePackage() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        PackageDetailsEntity packageDetails = new PackageDetailsEntity();
        when(packageUtils.findById(1)).thenReturn(packageEntity);
        when(packageDetailsUtils.findByPackageOfferId(1)).thenReturn(packageDetails);

        //when
        ResponseEntity<?> response = packageService.deletePackage(1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}