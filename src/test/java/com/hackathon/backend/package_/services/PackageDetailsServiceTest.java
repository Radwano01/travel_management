package com.hackathon.backend.package_.services;

import com.hackathon.backend.dto.packageDto.EditPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.GetPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.GetPackageDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.services.package_.PackageDetailsService;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackageDetailsServiceTest {

    @Mock
    PackageUtils packageUtils;

    @Mock
    PackageDetailsUtils packageDetailsUtils;

    @Mock
    S3Service s3Service;

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

        GetPackageDto getPackageDto = (GetPackageDto) response.getBody();
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(getPackageDto);
        assertEquals(packageEntity.getPackageName(), getPackageDto.getPackageName());
        assertEquals(packageEntity.getMainImage(), getPackageDto.getMainImage());
        assertEquals(packageEntity.getPrice(), getPackageDto.getPrice());
        assertEquals(packageEntity.getRate(), getPackageDto.getRate());
        assertEquals(packageEntity.getPackageDetails().getImageOne(), getPackageDto.getPackageDetails().getImageOne());
        assertEquals(packageEntity.getPackageDetails().getImageTwo(), getPackageDto.getPackageDetails().getImageTwo());
        assertEquals(packageEntity.getPackageDetails().getImageThree(), getPackageDto.getPackageDetails().getImageThree());
        assertEquals(packageEntity.getPackageDetails().getDescription(), getPackageDto.getPackageDetails().getDescription());
        assertEquals(packageEntity.getPackageDetails().getRoadmaps(), getPackageDto.getRoadmaps());
        assertEquals(packageEntity.getPackageDetails().getBenefits(), getPackageDto.getBenefits());
    }

    @Test
    void editPackageDetails() {
        //given
        int packageDetailsId = 1;
        EditPackageDetailsDto editPackageDetailsDto = new EditPackageDetailsDto(
                new MockMultipartFile("imageOne", "imageOne.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageTwo", "imageTwo.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageThree", "imageThree.jpg", "image/jpeg", new byte[0]),
                "testDesc"
        );

        PackageDetailsEntity packageDetailsEntity = new PackageDetailsEntity();
        packageDetailsEntity.setId(packageDetailsId);
        packageDetailsEntity.setImageOne("testImageOne");
        packageDetailsEntity.setImageTwo("testImageTwo");
        packageDetailsEntity.setImageThree("testImageThree");
        packageDetailsEntity.setDescription("testDesc");


        //behavior
        when(packageDetailsUtils.findById(packageDetailsId)).thenReturn(packageDetailsEntity);
        when(s3Service.uploadFile(editPackageDetailsDto.getImageOne())).thenReturn("imageOne");
        when(s3Service.uploadFile(editPackageDetailsDto.getImageTwo())).thenReturn("imageTwo");
        when(s3Service.uploadFile(editPackageDetailsDto.getImageThree())).thenReturn("imageThree");


        //when
        ResponseEntity<?> response = packageDetailsService.editPackageDetails(packageDetailsId, editPackageDetailsDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("imageOne", packageDetailsEntity.getImageOne());
        assertEquals("imageTwo", packageDetailsEntity.getImageTwo());
        assertEquals("imageThree", packageDetailsEntity.getImageThree());
        assertEquals("testDesc", packageDetailsEntity.getDescription());
    }
}