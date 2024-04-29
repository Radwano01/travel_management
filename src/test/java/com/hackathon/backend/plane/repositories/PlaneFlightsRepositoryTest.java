package com.hackathon.backend.plane.repositories;

import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.repositories.plane.PlaneFlightsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlaneFlightsRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private PlaneFlightsRepository planeFlightsRepository;

    @BeforeEach
    void setUp() {
        PlaneFlightsEntity planeFlights = new PlaneFlightsEntity();
        CountryEntity countryOne = new CountryEntity();
        countryOne.setId(1);
        countryOne.setCountry("United Kingdom");

        CountryEntity countryTwo = new CountryEntity();
        countryTwo.setId(2);
        countryTwo.setCountry("United Status");

        countryRepository.saveAll(List.of(countryOne,countryTwo));

        planeFlights.setId(1L);
        planeFlights.setDepartureCountry(countryOne);
        planeFlights.setDestinationCountry(countryTwo);
        planeFlightsRepository.save(planeFlights);
    }

    @AfterEach
    void tearDown() {
        planeFlightsRepository.deleteAll();
    }

    @Test
    void findAllByDepartureCountryIdAndDestinationCountryId() {
        //given
        int departureId = 1;
        int destinationId = 1;

        //when
        List<PlaneFlightsEntity> planeFlightsEntities = planeFlightsRepository
                .findAllByDepartureCountryIdAndDestinationCountryId(departureId, destinationId);
        //then
        assertNotNull(planeFlightsEntities);
    }
}