package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.CountryDetailsDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.services.country.CountryDetailsService;
import com.hackathon.backend.utilities.country.CountryDetailsUtils;
import com.hackathon.backend.utilities.country.CountryUtils;
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
class CountryDetailsServiceTest {

    @Mock
    CountryUtils countryUtils;

    @Mock
    CountryDetailsUtils countryDetailsUtils;

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
        CountryDetailsDto countryDetailsDto = (CountryDetailsDto) response.getBody();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert countryDetailsDto != null;
        assertEquals(country.getCountryDetails().getImageOne(), countryDetailsDto.getImageOne());
        assertEquals(country.getCountryDetails().getImageTwo(), countryDetailsDto.getImageTwo());
        assertEquals(country.getCountryDetails().getImageThree(), countryDetailsDto.getImageThree());
        assertEquals(country.getCountryDetails().getDescription(), countryDetailsDto.getDescription());
        assertEquals(country.getCountry(), countryDetailsDto.getCountry());
        assertEquals(country.getMainImage(), countryDetailsDto.getCountryMainImage());
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

        CountryDetailsDto editedCountryDetailsDto = new CountryDetailsDto();
        editedCountryDetailsDto.setImageOne("testImageOne1");
        editedCountryDetailsDto.setImageTwo("testImageTwo1");
        editedCountryDetailsDto.setImageThree("testImageThree1");
        editedCountryDetailsDto.setDescription("testDesc1");

        //behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);

        //when
        ResponseEntity<?> response = countryDetailsService.editCountryDetails(countryId,editedCountryDetailsDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(editedCountryDetailsDto.getImageOne(), countryDetails.getImageOne());
        assertEquals(editedCountryDetailsDto.getImageTwo(), countryDetails.getImageTwo());
        assertEquals(editedCountryDetailsDto.getImageThree(), countryDetails.getImageThree());
        assertEquals(editedCountryDetailsDto.getDescription(), countryDetails.getDescription());
    }
}