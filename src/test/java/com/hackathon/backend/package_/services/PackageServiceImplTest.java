package com.hackathon.backend.package_.services;

import com.hackathon.backend.dto.packageDto.CreatePackageDto;
import com.hackathon.backend.dto.packageDto.EditPackageDto;
import com.hackathon.backend.dto.packageDto.GetEssentialPackageDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.services.package_.impl.PackageServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PackageServiceImplTest {

    @Mock
    CountryRepository countryRepository;

    @Mock
    S3Service s3Service;

    @InjectMocks
    PackageServiceImpl packageServiceImpl;

    @Test
    void createPackage_Success() {
        //given
        int countryId = 1;
        MockMultipartFile image =
                new MockMultipartFile("mainImage", "mainImage.jpg", "image/jpeg", "random-image-content".getBytes());

        CreatePackageDto createPackageDto = new CreatePackageDto();
        createPackageDto.setPackageName("Luxury Package");
        createPackageDto.setPrice(2000);
        createPackageDto.setMainImage(image);
        createPackageDto.setRate(4);

        CountryEntity country = new CountryEntity();
        country.setId(countryId);

        //behavior
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));
        when(s3Service.uploadFile(image)).thenReturn("uploaded-image-url");

        //when
        ResponseEntity<String> response = packageServiceImpl.createPackage(countryId, createPackageDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(countryRepository, times(1)).save(any(CountryEntity.class));
    }

    @Test
    void getPackagesByCountry_Success() {
        //given
        int countryId = 1;
        List<GetEssentialPackageDto> packages = Arrays.asList(new GetEssentialPackageDto(), new GetEssentialPackageDto());

        //behavior
        when(countryRepository.findPackagesByCountryId(countryId)).thenReturn(packages);

        //when
        ResponseEntity<List<GetEssentialPackageDto>> response = packageServiceImpl.getPackagesByCountry(countryId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(packages.size(), response.getBody().size());
    }

    @Test
    void editPackage_Success() {
        //given
        int countryId = 1;
        int packageId = 1;

        MockMultipartFile image =
                new MockMultipartFile("mainImage", "mainImage.jpg", "image/jpeg", "random-image-content".getBytes());
        EditPackageDto editPackageDto = new EditPackageDto();
        editPackageDto.setPackageName("Updated Package");
        editPackageDto.setPrice(2500);
        editPackageDto.setMainImage(image);
        editPackageDto.setRate(4);

        CountryEntity country = new CountryEntity();
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setMainImage("old-image.png");

        //behavior
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));
        when(countryRepository.findPackageByCountryIdAndPackageId(countryId, packageId)).thenReturn(Optional.of(packageEntity));
        when(s3Service.uploadFile(image)).thenReturn("updated-image-url");

        //when
        ResponseEntity<String> response = packageServiceImpl.editPackage(countryId, packageId, editPackageDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(countryRepository, times(1)).save(country);
    }

    @Test
    void editPackage_ShouldReturnBadRequest_WhenSentEmptyData() {
        //given
        int countryId = 1;
        int packageId = 1;

        EditPackageDto editPackageDto = new EditPackageDto();

        // when
        ResponseEntity<String> response = packageServiceImpl.editPackage(countryId, packageId, editPackageDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("you sent an empty data to change", response.getBody());
    }


    @Test
    void deletePackage_Success() {
        int countryId = 1;
        int packageId = 1;

        CountryEntity country = new CountryEntity();
        PackageEntity packageEntity = new PackageEntity();
        PackageDetailsEntity packageDetails = new PackageDetailsEntity();
        packageDetails.setImageOne("image1.png");
        packageDetails.setImageTwo("image2.png");
        packageDetails.setImageThree("image3.png");
        packageEntity.setPackageDetails(packageDetails);
        packageEntity.setMainImage("mainImage.png");

        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));
        when(countryRepository.findPackageByCountryIdAndPackageId(countryId, packageId)).thenReturn(Optional.of(packageEntity));

        ResponseEntity<String> response = packageServiceImpl.deletePackage(countryId, packageId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(s3Service, times(4)).deleteFile(anyString()); // 3 images + main image
        verify(countryRepository, times(1)).save(country);
    }
}