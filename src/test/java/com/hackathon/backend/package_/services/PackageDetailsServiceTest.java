package com.hackathon.backend.package_.services;

import com.hackathon.backend.dto.packageDto.PackageDetailsDto;
import com.hackathon.backend.dto.packageDto.PackageDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.services.package_.PackageDetailsService;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackageDetailsServiceTest {

    @Mock
    PackageUtils packageUtils;

    @Mock
    PackageDetailsUtils packageDetailsUtils;

    @InjectMocks
    PackageDetailsService packageDetailsService;

    @Test
    void getSinglePackageDetails() {
        //given
        int packageId = 1;
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);
        packageEntity.setPackageName("testPackage");
        packageEntity.setMainImage("testImage");
        packageEntity.setPrice(100);
        packageEntity.setRate(2.50f);

        PackageDetailsEntity packageDetailsEntity = new PackageDetailsEntity();
        packageDetailsEntity.setId(1);
        packageDetailsEntity.setImageOne("testImageOne");
        packageDetailsEntity.setImageTwo("testImageTwo");
        packageDetailsEntity.setImageThree("testImageThree");
        packageDetailsEntity.setDescription("testDesc");
        packageDetailsEntity.getRoadmaps().add(new RoadmapEntity());
        packageDetailsEntity.getBenefits().add(new BenefitEntity());

        packageEntity.setPackageDetails(packageDetailsEntity);

        //behavior
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);

        //when
        ResponseEntity<?> response = packageDetailsService.getSinglePackageDetails(packageId);

        PackageDto packageDto = (PackageDto) response.getBody();
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(packageDto);
        assertEquals(packageEntity.getPackageName(), packageDto.getPackageName());
        assertEquals(packageEntity.getMainImage(), packageDto.getMainImage());
        assertEquals(packageEntity.getPrice(), packageDto.getPrice());
        assertEquals(packageEntity.getRate(), packageDto.getRate());
        assertEquals(packageEntity.getPackageDetails().getImageOne(), packageDto.getPackageDetails().getImageOne());
        assertEquals(packageEntity.getPackageDetails().getImageTwo(), packageDto.getPackageDetails().getImageTwo());
        assertEquals(packageEntity.getPackageDetails().getImageThree(), packageDto.getPackageDetails().getImageThree());
        assertEquals(packageEntity.getPackageDetails().getDescription(), packageDto.getPackageDetails().getDescription());
        assertEquals(packageEntity.getPackageDetails().getRoadmaps(), packageDto.getRoadmaps());
        assertEquals(packageEntity.getPackageDetails().getBenefits(), packageDto.getBenefits());
    }

    @Test
    void editPackageDetails() {
        //given
        int packageDetailsId = 1;
        PackageDetailsDto packageDetailsDto = new PackageDetailsDto();
        packageDetailsDto.setImageOne("testImageOne1");
        packageDetailsDto.setImageTwo("testImageTwo1");
        packageDetailsDto.setImageThree("testImageThree1");
        packageDetailsDto.setDescription("testDescription1");


        PackageDetailsEntity packageDetailsEntity = new PackageDetailsEntity();
        packageDetailsEntity.setId(packageDetailsId);
        packageDetailsEntity.setImageOne("testImageOne");
        packageDetailsEntity.setImageTwo("testImageTwo");
        packageDetailsEntity.setImageThree("testImageThree");
        packageDetailsEntity.setDescription("testDesc");


        //behavior
        when(packageDetailsUtils.findById(packageDetailsId)).thenReturn(packageDetailsEntity);

        //when
        ResponseEntity<?> response = packageDetailsService.editPackageDetails(packageDetailsId, packageDetailsDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(packageDetailsDto.getImageOne(), packageDetailsEntity.getImageOne());
        assertEquals(packageDetailsDto.getImageTwo(), packageDetailsEntity.getImageTwo());
        assertEquals(packageDetailsDto.getImageThree(), packageDetailsEntity.getImageThree());
        assertEquals(packageDetailsDto.getDescription(), packageDetailsEntity.getDescription());
    }
}