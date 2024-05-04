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
    private CountryRepository countryRepository;

    @BeforeEach
    void setUp() {
        CountryEntity country = new CountryEntity();
        country.setId(1);
        country.setCountry("ExistingCountryName");
        countryRepository.save(country);
    }

    @AfterEach
    void tearDown() {
        countryRepository.deleteAll();
    }

    @Test
    void existsByCountry() {
        // given
        String existingCountryName = "ExistingCountryName";

        // when
        boolean exists = countryRepository.existsByCountry(existingCountryName);

        // then
        assertTrue(exists);
    }

    @Test
    void findAllCountries() {
        // given
        // Countries exist in the database

        // when
        List<CountryDto> countries = countryRepository.findAllCountries();

        // then
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
    }
}
