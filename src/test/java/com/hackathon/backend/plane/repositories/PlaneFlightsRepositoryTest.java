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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class PlaneFlightsRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private PlaneFlightsRepository planeFlightsRepository;

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