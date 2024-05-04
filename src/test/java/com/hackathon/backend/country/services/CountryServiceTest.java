package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.CountryDetailsDto;
import com.hackathon.backend.dto.countryDto.CountryDto;
import com.hackathon.backend.dto.countryDto.CountryWithDetailsDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.services.country.CountryService;
import com.hackathon.backend.utilities.country.CountryDetailsUtils;
import com.hackathon.backend.utilities.country.CountryUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryUtils countryUtils;

    @Mock
    private CountryDetailsUtils countryDetailsUtils;

    private CountryService countryService;

    @BeforeEach
    void setUp() {
        countryService = new CountryService(countryUtils,
                countryDetailsUtils);
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
        countryDetailsDto.setCountryMainImage("main_image_url");
        countryDetailsDto.setImageOne("image_one_url");
        countryDetailsDto.setImageTwo("image_two_url");
        countryDetailsDto.setImageThree("image_three_url");
        countryDetailsDto.setDescription("about country description");

        CountryWithDetailsDto countryWithDetailsDto = new CountryWithDetailsDto();
        countryWithDetailsDto.setCountry("United Kingdom");
        countryWithDetailsDto.setMainImage("image_url");
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
        countryDetailsDto.setCountryMainImage("main_image_url");
        countryDetailsDto.setImageOne("image_one_url");
        countryDetailsDto.setImageTwo("image_two_url");
        countryDetailsDto.setImageThree("image_three_url");
        countryDetailsDto.setDescription("about country description");

        CountryWithDetailsDto countryWithDetailsDto = new CountryWithDetailsDto();
        countryWithDetailsDto.setCountry("United Kingdom");
        countryWithDetailsDto.setMainImage("image_url");
        countryWithDetailsDto.setCountryDetails(countryDetailsDto);

        when(countryUtils.existsByCountry("United Kingdom")).thenReturn(false)
                .thenReturn(true);
        //when
        ResponseEntity<?> response = countryService.createCountry(countryWithDetailsDto);
        ResponseEntity<?> secondResponse = countryService.createCountry(countryWithDetailsDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.CONFLICT, secondResponse.getStatusCode());
    }

    @Test
    void getCountries() {
        //given
        List<CountryDto> countryEntities = new ArrayList<>();
        CountryDto countryDto = new CountryDto();
        countryDto.setId(1);
        countryDto.setCountry("uk");
        countryDto.setMainImage("image_url");
        countryEntities.add(countryDto);
        when(countryUtils.findAllCountries()).thenReturn(countryEntities);
        //when
        ResponseEntity<?> response = countryService.getCountries();
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<CountryDto> mappedCountries = (List<CountryDto>) response.getBody();
        assertSame(countryDto, mappedCountries.get(0));
    }

    @Test
    void editCountry() {
        //given
        CountryDetailsDto countryDetailsDto = new CountryDetailsDto();
        countryDetailsDto.setCountry("United Kingdom");
        countryDetailsDto.setCountryMainImage("main_image_url");
        countryDetailsDto.setImageOne("image_one_url");
        countryDetailsDto.setImageTwo("image_two_url");
        countryDetailsDto.setImageThree("image_three_url");
        countryDetailsDto.setDescription("about country description");

        CountryWithDetailsDto countryWithDetailsDto = new CountryWithDetailsDto();
        countryWithDetailsDto.setCountry("United Kingdom");
        countryWithDetailsDto.setMainImage("image_url");
        countryWithDetailsDto.setCountryDetails(countryDetailsDto);

        countryService.createCountry(countryWithDetailsDto);
        int countryId = 1;
        when(countryUtils.findCountryById(countryId)).thenReturn(new CountryEntity());
        //new Data
        CountryDto newCountryDto = new CountryDto();
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
        countryUtils.save(country);
        countryDetailsUtils.save(countryDetails);

        when(countryUtils.findCountryById(1)).thenReturn(country);
        when(countryDetailsUtils.findByCountryId(1)).thenReturn(countryDetails);
        //when
        ResponseEntity<?> response = countryService.deleteCountry(1);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}