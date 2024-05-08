package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.placeDto.PlaceDetailsDto;
import com.hackathon.backend.dto.countryDto.placeDto.PlaceDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.services.country.PlaceService;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.country.PlaceDetailsUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
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
class PlaceServiceTest {

    @Mock
    private CountryUtils countryUtils;
    @Mock
    private PlaceUtils placeUtils;

    @Mock
    private PlaceDetailsUtils placeDetailsUtils;

    private PlaceService placeService;


    @BeforeEach
    void setUp() {
        placeService = new PlaceService(
                countryUtils,
                placeUtils,
                placeDetailsUtils);
    }

    @AfterEach
    void tearDown() {
        placeUtils.deleteAll();
    }

    @Test
    void createPlace() {
        //given
        int countryId = 1;
        CountryEntity country = new CountryEntity();

        PlaceDetailsDto placeDetails = new PlaceDetailsDto();

        PlaceDto place = new PlaceDto();
        place.setPlace("London");
        place.setMainImage("url_image");
        place.setPlaceDetails(placeDetails);

        when(countryUtils.findCountryById(countryId)).thenReturn(country);
        //when
        ResponseEntity<?> response = placeService.createPlace(countryId,place);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getPlace(){
        //given
        int countryId = 1;

        //when
        ResponseEntity<?> response = placeService.getPlacesByCountry(countryId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response);
    }

    @Test
    void editPlace() {
        //given
        int countryId = 1;
        int placeId = 1;
        PlaceDto placeDto = new PlaceDto();
        placeDto.setPlace("London");
        placeDto.setMainImage("image_url");
        placeDto.setCountry("United Kingdom");
        when(countryUtils.findCountryById(countryId)).thenReturn(new CountryEntity());
        when(placeUtils.findById(placeId)).thenReturn(new PlaceEntity());

        //when
        ResponseEntity<?> response = placeService.editPlace(countryId,placeId,placeDto);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deletePlace() {
        //given
        int placeId = 1;

        when(placeUtils.findById(placeId)).thenReturn(new PlaceEntity());
        //when
        ResponseEntity<?> response = placeService.deletePlace(placeId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}