package com.hackathon.backend.package_.services;

import com.hackathon.backend.dto.packageDto.PackageDetailsDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.services.package_.PackageDetailsService;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import org.junit.jupiter.api.AfterEach;
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
class PackageDetailsServiceTest {

    @Mock
    private PackageDetailsUtils packageDetailsUtils;

    @Mock
    private PackageUtils packageUtils;

    private PackageDetailsService packageDetailsService;

    @BeforeEach
    void setUp() {
        packageDetailsService = new PackageDetailsService(
                packageDetailsUtils,
                packageUtils);
    }

    @Test
    void getSinglePackageDetails() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        PackageDetailsEntity packageDetailsEntity = new PackageDetailsEntity();
        packageDetailsEntity.setId(1);
        packageEntity.setPackageDetails(packageDetailsEntity);
        when(packageUtils.findById(1)).thenReturn(packageEntity);

        //when
        ResponseEntity<?> response = packageDetailsService.getSinglePackageDetails(1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editPackageDetails() {
        //given
        PackageDetailsEntity packageDetailsEntity = new PackageDetailsEntity();
        when(packageDetailsUtils.findById(1)).thenReturn(packageDetailsEntity);

        //when
        ResponseEntity<?> response = packageDetailsService
                .editPackageDetails(1, new PackageDetailsDto());

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}