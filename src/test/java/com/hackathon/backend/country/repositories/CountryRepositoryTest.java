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
    void setUp(){
        String country = "United Kingdom";
        CountryEntity countryEntity = new CountryEntity(country);
        countryRepository.save(countryEntity);
    }

    @AfterEach
    void tearDown(){
        countryRepository.deleteAll();
    }

    @Test
    void existsByCountry() {
        //given
        String country = "United Kingdom";
        //when
        boolean exists = countryRepository.existsByCountry(country);
        //then
        assertTrue(exists);
    }

    @Test
    void findByCountry(){
        //given
        String country = "United Kingdom";
        //when
        CountryEntity countryEntity = countryRepository.findByCountry(country);
        //then
        assertEquals(country, countryEntity.getCountry());
    }

    @Test
    void findAllCountries(){
        //when
        List<CountryDto> countries = countryRepository.findAllCountries();
        //then
        assertNotNull(countries);
        assertSame(1, countries.size());
    }
}