package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.CreateCountryDto;
import com.hackathon.backend.dto.countryDto.EditCountryDto;
import com.hackathon.backend.dto.countryDto.GetCountryDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.services.country.CountryService;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private CountryService countryService;

    private CreateCountryDto createCountryDto;
    private EditCountryDto editCountryDto;

    @Mock
    private CountryEntity countryEntity;

    @BeforeEach
    void setUp() {
        // Initialize the DTOs and Entities
        createCountryDto = new CreateCountryDto();
        createCountryDto.setCountry("Test Country");

        editCountryDto = new EditCountryDto();
        editCountryDto.setCountry("Edited Country");
        editCountryDto.setMainImage
                (new MockMultipartFile("mainImage", "mainImage.jpg", "image/jpeg", "random-image-content".getBytes()));

        countryEntity = new CountryEntity("Test Country", "testMainImage.jpg");
        countryEntity.setId(1);
    }

    @AfterEach
    void tearDown(){
        countryRepository.deleteAll();
    }

    @Test
    void createCountry() {
        // behavior
        when(countryRepository.existsByCountry(createCountryDto.getCountry())).thenReturn(false);
        when(s3Service.uploadFile(createCountryDto.getMainImage())).thenReturn("uploadedMainImage.jpg");
        when(countryRepository.save(any(CountryEntity.class))).thenReturn(countryEntity);

        // when
        ResponseEntity<String> response = countryService.createCountry(createCountryDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(countryRepository, times(2)).save(any(CountryEntity.class));
    }

    @Test
    void createCountry_CountryAlreadyExists() {
        //behavior
        when(countryRepository.existsByCountry(createCountryDto.getCountry())).thenReturn(true);

        //when
        ResponseEntity<String> response = countryService.createCountry(createCountryDto);

        //then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Country already exists: Test Country", response.getBody());
        verify(countryRepository, never()).save(any(CountryEntity.class));
    }

    @Test
    void getAllCountries() {
        //given
        List<GetCountryDto> countries = List.of(new GetCountryDto(1, "Test Country", "testImage.jpg"));

        //behavior
        when(countryRepository.findAllCountries()).thenReturn(countries);

        // when
        ResponseEntity<List<GetCountryDto>> response = countryService.getAllCountries();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void editCountry() {
        //given
        String oldMainImage = "testMainImage.jpg";
        String newMainImage = "newMainImage.jpg";

        //behavior
        when(countryRepository.findById(countryEntity.getId())).thenReturn(Optional.of(countryEntity));
        when(countryRepository.existsByCountry(editCountryDto.getCountry())).thenReturn(false);
        when(s3Service.uploadFile(editCountryDto.getMainImage())).thenReturn(newMainImage);

        // when
        ResponseEntity<String> response = countryService.editCountry(countryEntity.getId(), editCountryDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(s3Service).deleteFile(oldMainImage);
        verify(s3Service).uploadFile(editCountryDto.getMainImage());
        verify(countryRepository).save(countryEntity);
    }


    @Test
    void editCountry_NoDataSent() {
        // given
        EditCountryDto emptyEditCountryDto = new EditCountryDto();

        // when
        ResponseEntity<String> response = countryService.editCountry(countryEntity.getId(), emptyEditCountryDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("you sent an empty data to change", response.getBody());
        verify(countryRepository, never()).save(any(CountryEntity.class));
    }

    @Test
    void deleteCountry() {
        // behavior
        when(countryRepository.findById(countryEntity.getId())).thenReturn(Optional.of(countryEntity));

        // when
        ResponseEntity<String> response = countryService.deleteCountry(countryEntity.getId());

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(s3Service).deleteFile(anyString());
        verify(countryRepository).delete(countryEntity);
    }

    @Test
    void deleteCountry_CountryNotFound() {
        // behavior
        when(countryRepository.findById(anyInt())).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class, () -> countryService.deleteCountry(1));
    }
}
