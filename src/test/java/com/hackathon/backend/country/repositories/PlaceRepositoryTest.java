package com.hackathon.backend.country.repositories;

import com.hackathon.backend.dto.countryDto.placeDto.EssentialPlaceDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.repositories.country.PlaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlaceRepositoryTest {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    PlaceRepository placeRepository;

    @BeforeEach
    void setUp() {
        CountryEntity country = new CountryEntity(
                "testCountry",
                "testImage"
        );
        countryRepository.save(country);

        PlaceEntity place = new PlaceEntity(
                "testPlace",
                "testImage",
                country
        );
        placeRepository.save(place);
    }

    @AfterEach
    void tearDown() {
        placeRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    void findPlacesByCountryId() {
        //given
        int countryId = countryRepository.findAll().get(0).getId();

        //when
        List<EssentialPlaceDto> places = placeRepository.findPlacesByCountryId(countryId);

        //then
        assertEquals(places.get(0).getPlace(), "testPlace");
        assertEquals(places.get(0).getMainImage(), "testImage");
    }
}