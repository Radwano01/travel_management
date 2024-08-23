package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.EditCountryDetailsDto;
import com.hackathon.backend.dto.countryDto.GetCountryWithCountryDetailsDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.services.country.CountryDetailsService;
import com.hackathon.backend.utilities.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryDetailsServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private CountryDetailsService countryDetailsService;

    private CountryEntity country;

    @BeforeEach
    void setUp() {
        CountryDetailsEntity countryDetails = new CountryDetailsEntity();
        countryDetails.setId(1);
        countryDetails.setImageOne("oldImageOne.jpg");
        countryDetails.setImageTwo("oldImageTwo.jpg");
        countryDetails.setImageThree("oldImageThree.jpg");
        countryDetails.setDescription("Old description");

        country = new CountryEntity();
        country.setId(1);
        country.setCountry("Test Country");
        country.setCountryDetails(countryDetails);
    }

    @AfterEach
    void tearDown(){
        countryRepository.deleteAll();
    }

    @Test
    void getSingleCountryDetails_CountryExists_ReturnsCountryDetails() {
        //given
        int countryId = 1;

        //behavior
        when(countryRepository.findCountryWithCountryDetailsByCountryId(countryId))
                .thenReturn(Optional.of(new GetCountryWithCountryDetailsDto(
                        1, "Test Country", "mainImage.jpg",
                        "imageOne.jpg", "imageTwo.jpg", "imageThree.jpg", "Description")));

        //when
        ResponseEntity<GetCountryWithCountryDetailsDto> response = countryDetailsService.getSingleCountryDetails(countryId);

        //then
        assertNotNull(response);
        verify(countryRepository, times(1)).findCountryWithCountryDetailsByCountryId(countryId);
    }

    @Test
    void getSingleCountryDetails_CountryDoesNotExist_ThrowsEntityNotFoundException() {
        //given
        int countryId = 1;
        when(countryRepository.findCountryWithCountryDetailsByCountryId(countryId)).thenReturn(Optional.empty());

        //then
        assertThrows(EntityNotFoundException.class, () -> countryDetailsService.getSingleCountryDetails(countryId));
        verify(countryRepository, times(1)).findCountryWithCountryDetailsByCountryId(countryId);
    }

    @Test
    void editCountryDetails_ValidEdit_ReturnsUpdatedCountryDetails() {
        //given
        int countryId = 1;
        EditCountryDetailsDto editDto = new EditCountryDetailsDto();
        editDto.setImageOne(new MockMultipartFile("imageOne", "newImageOne.jpg", "image/jpeg", "image content".getBytes()));
        editDto.setDescription("New description");

        //behavior
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));
        when(s3Service.uploadFile(editDto.getImageOne())).thenReturn("newImageOne.jpg");

        //when
        ResponseEntity<String> response = countryDetailsService.editCountryDetails(countryId, editDto);

        //then
        assertNotNull(response);
        assertEquals("New description", country.getCountryDetails().getDescription());
        assertEquals("newImageOne.jpg", country.getCountryDetails().getImageOne());
        verify(s3Service, times(1)).deleteFile("oldImageOne.jpg");
        verify(s3Service, times(1)).uploadFile(editDto.getImageOne());
        verify(countryRepository, times(1)).save(country);
    }

    @Test
    void editCountryDetails_EmptyEdit_ThrowsBadRequestException() {
        //given
        int countryId = 1;
        EditCountryDetailsDto editDto = new EditCountryDetailsDto();

        //then
        ResponseEntity<String> response = countryDetailsService.editCountryDetails(countryId, editDto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
