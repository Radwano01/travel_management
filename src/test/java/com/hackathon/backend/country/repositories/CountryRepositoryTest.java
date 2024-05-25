package com.hackathon.backend.country.repositories;

import com.hackathon.backend.dto.countryDto.CountryDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CountryRepositoryTest {

    @Autowired
    CountryRepository countryRepository;

    @BeforeEach
    void setUp(){
        CountryEntity country = new CountryEntity(
                "testCountry",
                "testImage"
        );
        countryRepository.save(country);
    }

    @AfterEach
    void tearDown(){
        countryRepository.deleteAll();
    }

    @Test
    void existsByCountry() {
        //given
        String country = "testCountry";

        //when
        boolean exists = countryRepository.existsByCountry(country);

        //then
        assertTrue(exists);
    }

    @Test
    void findAllCountries() {

        //when
        List<CountryDto> response = countryRepository.findAllCountries();

        //then
        assertEquals(response.get(0).getCountry(), "testCountry");
        assertEquals(response.get(0).getMainImage(), "testImage");
    }
}