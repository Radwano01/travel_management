package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.CountryDetailsDto;
import com.hackathon.backend.dto.countryDto.CountryDto;
import com.hackathon.backend.dto.countryDto.CountryWithDetailsDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.services.country.CountryService;
import com.hackathon.backend.utilities.country.CountryDetailsUtils;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryUtils countryUtils;

    @Mock
    private CountryDetailsUtils countryDetailsUtils;

    @Mock
    private PlaceUtils placeUtils;

    @Mock
    private HotelUtils hotelUtils;

    @Mock
    private PackageUtils packageUtils;

    @Mock
    private PlaneFlightsUtils planeFlightsUtils;

    private CountryService countryService;

    @BeforeEach
    void setUp() {
        countryService = new CountryService(
                countryUtils,
                countryDetailsUtils,
                placeUtils,
                hotelUtils,
                packageUtils,
                planeFlightsUtils
        );
    }

    @AfterEach
    void tearDown() {
        countryUtils.deleteAll();
        countryDetailsUtils.deleteAll();
    }

    @Test
    void createCountry() {
        //given
        CountryDetailsDto countryDetailsDto = new CountryDetailsDto();
        countryDetailsDto.setCountry("United Kingdom");

        CountryWithDetailsDto countryWithDetailsDto = new CountryWithDetailsDto();
        countryWithDetailsDto.setCountry("United Kingdom");
        countryWithDetailsDto.setCountryDetails(countryDetailsDto);
        //when
        ResponseEntity<?> response = countryService.createCountry(countryWithDetailsDto);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void existsCountry(){
        //given
        CountryDetailsDto countryDetailsDto = new CountryDetailsDto();
        countryDetailsDto.setCountry("United Kingdom");


        CountryWithDetailsDto countryWithDetailsDto = new CountryWithDetailsDto();
        countryWithDetailsDto.setCountry("United Kingdom");
        countryWithDetailsDto.setCountryDetails(countryDetailsDto);

        when(countryUtils.existsByCountry("United Kingdom")).thenReturn(true);
        //when
        ResponseEntity<?> response = countryService.createCountry(countryWithDetailsDto);

        //then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void getCountries() {
        //given
        List<CountryDto> countryEntities = Collections.singletonList(new CountryDto());
        when(countryUtils.findAllCountries()).thenReturn(countryEntities);

        //when
        ResponseEntity<?> response = countryService.getCountry();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(countryEntities, response.getBody());
    }

    @Test
    void editCountry() {
        //given
        int countryId = 1;

        CountryDetailsDto countryDetailsDto = new CountryDetailsDto();
        countryDetailsDto.setCountry("United Kingdom");

        CountryWithDetailsDto countryWithDetailsDto = new CountryWithDetailsDto();
        countryWithDetailsDto.setCountry("United Kingdom");
        countryWithDetailsDto.setCountryDetails(countryDetailsDto);

        countryService.createCountry(countryWithDetailsDto);

        when(countryUtils.findCountryById(countryId)).thenReturn(new CountryEntity());
        //new Data
        CountryWithDetailsDto newCountryDto = new CountryWithDetailsDto();
        newCountryDto.setCountry("United Kingdom");
        newCountryDto.setMainImage("url_image");
        //when
        ResponseEntity<?> response = countryService.editCountry(countryId,newCountryDto);
        //
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void deleteCountry() {
        //given
        CountryEntity country = new CountryEntity();
        country.setId(1);
        CountryDetailsEntity countryDetails = new CountryDetailsEntity();
        countryDetails.setId(1);
        countryDetails.setCountry(country);
        when(countryUtils.findCountryById(1)).thenReturn(country);

        //when
        ResponseEntity<?> response = countryService.deleteCountry(1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}