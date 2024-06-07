package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.EditCountryDetailsDto;
import com.hackathon.backend.dto.countryDto.GetCountryDetailsDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.services.country.CountryDetailsService;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryDetailsUtils;
import com.hackathon.backend.utilities.country.CountryUtils;
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
class CountryDetailsServiceTest {

    @Mock
    CountryUtils countryUtils;

    @Mock
    CountryDetailsUtils countryDetailsUtils;

    @Mock
    S3Service s3Service;

    @InjectMocks
    CountryDetailsService countryDetailsService;

    @Test
    void getSingleCountryDetails() {
        //given
        int countryId = 1;
        CountryEntity country = new CountryEntity();
        country.setId(countryId);
        CountryDetailsEntity countryDetails = new CountryDetailsEntity(
                "testImageOne",
                "testImageTwo",
                "testImageThree",
                "testDesc",
                country

        );
        country.setCountryDetails(countryDetails);

        //behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);

        //when
        ResponseEntity<?> response = countryDetailsService.getSingleCountryDetails(countryId);
        GetCountryDetailsDto getCountryDetailsDto = (GetCountryDetailsDto) response.getBody();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert getCountryDetailsDto != null;
        assertEquals(country.getCountryDetails().getImageOne(), getCountryDetailsDto.getImageOne());
        assertEquals(country.getCountryDetails().getImageTwo(), getCountryDetailsDto.getImageTwo());
        assertEquals(country.getCountryDetails().getImageThree(), getCountryDetailsDto.getImageThree());
        assertEquals(country.getCountryDetails().getDescription(), getCountryDetailsDto.getDescription());
        assertEquals(country.getCountry(), getCountryDetailsDto.getCountry());
        assertEquals(country.getMainImage(), getCountryDetailsDto.getCountryMainImage());
    }

    @Test
    void editCountryDetails() {
        //given
        int countryId = 1;
        CountryEntity country = new CountryEntity();
        country.setId(countryId);

        CountryDetailsEntity countryDetails = new CountryDetailsEntity(
                "testImageOne",
                "testImageTwo",
                "testImageThree",
                "testDesc",
                country

        );
        country.setCountryDetails(countryDetails);


        EditCountryDetailsDto editCountryDetailsDto = new EditCountryDetailsDto(
                new MockMultipartFile("imageOne", "imageOne.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageTwo", "imageTwo.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageThree", "imageThree.jpg", "image/jpeg", new byte[0]),
                "testDesc1"
        );

        //behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);
        when(s3Service.uploadFile(editCountryDetailsDto.getImageOne())).thenReturn("imageOne");
        when(s3Service.uploadFile(editCountryDetailsDto.getImageTwo())).thenReturn("imageTwo");
        when(s3Service.uploadFile(editCountryDetailsDto.getImageThree())).thenReturn("imageThree");

        //when
        ResponseEntity<?> response = countryDetailsService.editCountryDetails(countryId, editCountryDetailsDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("imageOne", countryDetails.getImageOne());
        assertEquals("imageTwo", countryDetails.getImageTwo());
        assertEquals("imageThree", countryDetails.getImageThree());
        assertEquals("testDesc1", countryDetails.getDescription());
    }
}