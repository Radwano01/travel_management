package com.hackathon.backend.package_.services;

import com.hackathon.backend.dto.packageDto.EditPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.GetPackageANDPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.features.GetBenefitDto;
import com.hackathon.backend.dto.packageDto.features.GetRoadmapDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.repositories.package_.PackageDetailsRepository;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.services.package_.impl.PackageDetailsServiceImpl;
import com.hackathon.backend.utilities.S3Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PackageDetailsServiceImplTest {

    @Mock
    PackageRepository packageRepository;

    @Mock
    PackageDetailsRepository packageDetailsRepository;

    @Mock
    S3Service s3Service;

    @InjectMocks
    PackageDetailsServiceImpl packageDetailsServiceImpl;

    @Test
    void getSinglePackageDetails_ShouldReturnPackageDetails_WhenPackageExists() {
        // given
        int packageId = 1;

        PackageDetailsEntity packageDetails = new PackageDetailsEntity();
        packageDetails.setImageOne("imageOne.jpg");
        packageDetails.setImageTwo("imageTwo.jpg");
        packageDetails.setImageThree("imageThree.jpg");
        packageDetails.setDescription("Package description");

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);
        packageEntity.setPackageName("Test Package");
        packageEntity.setPrice(100);
        packageEntity.setRate(4);
        packageEntity.setMainImage("mainImage.jpg");
        packageEntity.setPackageDetails(packageDetails);

        //behavior
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));

        // when
        ResponseEntity<GetPackageANDPackageDetailsDto> response = packageDetailsServiceImpl.getSinglePackageDetails(packageId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(packageId, response.getBody().getId());
        assertEquals("Test Package", response.getBody().getPackageName());
        assertEquals(100.0, response.getBody().getPrice());
        assertEquals(4, response.getBody().getRate());
        assertEquals("mainImage.jpg", response.getBody().getMainImage());
        assertEquals("imageOne.jpg", response.getBody().getImageOne());
        assertEquals("imageTwo.jpg", response.getBody().getImageTwo());
        assertEquals("imageThree.jpg", response.getBody().getImageThree());
        assertEquals("Package description", response.getBody().getDescription());

        verify(packageRepository, times(1)).findById(packageId);
    }

    @Test
    void getRoadmapsFromPackage() {
        // given
        int packageId = 1;
        List<GetRoadmapDto> mockRoadmaps = Arrays.asList(
                new GetRoadmapDto(1, "Roadmap 1"),
                new GetRoadmapDto(2, "Roadmap 2")
        );

        //behavior
        when(packageDetailsRepository.findPackageDetailsRoadmapsByPackageId(packageId))
                .thenReturn(mockRoadmaps);

        // when
        ResponseEntity<List<GetRoadmapDto>> response = packageDetailsServiceImpl.getRoadmapsFromPackage(packageId);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Roadmap 1", response.getBody().get(0).getRoadmap());
    }

    @Test
    void getBenefitsFromPackage() {
        // given
        int packageId = 2;
        List<GetBenefitDto> mockBenefits = Arrays.asList(
                new GetBenefitDto(1, "Benefit 1"),
                new GetBenefitDto(2, "Benefit 2")
        );

        // behavior
        when(packageDetailsRepository.findPackageDetailsBenefitsByPackageId(packageId))
                .thenReturn(mockBenefits);

        // when
        ResponseEntity<List<GetBenefitDto>> response = packageDetailsServiceImpl.getBenefitsFromPackage(packageId);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Benefit 1", response.getBody().get(0).getBenefit());
    }


    @Test
    void editPackageDetails_ShouldUpdateAllFields_WhenAllFieldsAreProvided() {
        // given
        int packageId = 1;
        MockMultipartFile imageOne = new MockMultipartFile("imageOne", "imageOne.jpg", "image/jpeg", "random-image-content-1".getBytes());
        MockMultipartFile imageTwo = new MockMultipartFile("imageTwo", "imageTwo.jpg", "image/jpeg", "random-image-content-2".getBytes());
        MockMultipartFile imageThree = new MockMultipartFile("imageThree", "imageThree.jpg", "image/jpeg", "random-image-content-3".getBytes());

        EditPackageDetailsDto editPackageDetailsDto = new EditPackageDetailsDto(
                imageOne,
                imageTwo,
                imageThree,
                "New Description"
        );

        PackageDetailsEntity packageDetails = new PackageDetailsEntity();
        packageDetails.setImageOne("oldImageOne.jpg");
        packageDetails.setImageTwo("oldImageTwo.jpg");
        packageDetails.setImageThree("oldImageThree.jpg");
        packageDetails.setDescription("Old Description");

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageDetails(packageDetails);

        //behavior
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));
        when(s3Service.uploadFile(imageOne)).thenReturn("uploadedImageOne.jpg");
        when(s3Service.uploadFile(imageTwo)).thenReturn("uploadedImageTwo.jpg");
        when(s3Service.uploadFile(imageThree)).thenReturn("uploadedImageThree.jpg");

        // when
        ResponseEntity<String> response = packageDetailsServiceImpl.editPackageDetails(packageId, editPackageDetailsDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("uploadedImageOne.jpg"));
        assertTrue(response.getBody().contains("uploadedImageTwo.jpg"));
        assertTrue(response.getBody().contains("uploadedImageThree.jpg"));
        assertTrue(response.getBody().contains("New Description"));

        verify(s3Service).deleteFile("oldImageOne.jpg");
        verify(s3Service).deleteFile("oldImageTwo.jpg");
        verify(s3Service).deleteFile("oldImageThree.jpg");
        verify(packageRepository, times(1)).save(packageEntity);
    }

    @Test
    void editPackageDetails_ShouldReturnBadRequest_WhenSentEmptyData() {
        // given
        int packageId = 1;
        EditPackageDetailsDto editPackageDetailsDto = new EditPackageDetailsDto(); // All fields are null

        // when
        ResponseEntity<String> response = packageDetailsServiceImpl.editPackageDetails(packageId, editPackageDetailsDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("you sent an empty data to change", response.getBody());
        verify(packageRepository, never()).save(any()); // Ensure save is never called
        verify(s3Service, never()).deleteFile(anyString()); // Ensure no file deletion
    }
}