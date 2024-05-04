package com.hackathon.backend.country.repositories;

import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.repositories.country.CountryDetailsRepository;
import com.hackathon.backend.repositories.country.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CountryDetailsRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryDetailsRepository countryDetailsRepository;

    @Test
    void findByCountryId() {
        // given
        CountryEntity country = new CountryEntity(
                "test",
                "test"
        );
        country.setId(1);

        CountryDetailsEntity countryDetails = new CountryDetailsEntity(
                "test",
                "test",
                "test",
                "test",
                country
        );
        countryRepository.save(country);
        countryDetailsRepository.save(countryDetails);


        // when
        Optional<CountryDetailsEntity> found = countryDetailsRepository.findByCountryId(1);

        // then
        assertTrue(found.isPresent());
    }
}
