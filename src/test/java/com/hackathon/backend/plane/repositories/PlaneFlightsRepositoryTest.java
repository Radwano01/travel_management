package com.hackathon.backend.plane.repositories;

import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.repositories.plane.PlaneFlightsRepository;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlaneFlightsRepositoryTest {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    PlaneRepository planeRepository;

    @Autowired
    PlaneFlightsRepository planeFlightsRepository;

    @BeforeEach
    void setUp(){
        CountryEntity countryDeparture = new CountryEntity(
                "testCountry",
                "testImage"
        );
        countryRepository.save(countryDeparture);

        CountryEntity countryDestination = new CountryEntity(
                "testCountry",
                "testImage"
        );
        countryRepository.save(countryDestination);

        PlaneEntity plane = new PlaneEntity(
                "testName",
                100
        );
        planeRepository.save(plane);

        PlaneFlightsEntity planeFlights = new PlaneFlightsEntity(
                200,
                plane,
                countryDeparture,
                countryDestination,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2)
        );
        planeFlightsRepository.save(planeFlights);
    }

    @AfterEach
    void tearDown(){
        planeFlightsRepository.deleteAll();
        planeRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    void findAllByDepartureCountryIdAndDestinationCountryId(){
        //given
        int departureId = countryRepository.findAll().get(0).getId();
        int destinationId = countryRepository.findAll().get(1).getId();

        //when
        List<PlaneFlightsEntity> response = planeFlightsRepository
                .findAllByDepartureCountryIdAndDestinationCountryId(departureId,destinationId);

        //then
        assertEquals(response.get(0).getDepartureCountry().getCountry(), "testCountry");
        assertEquals(response.get(0).getDestinationCountry().getCountry(), "testCountry");
        assertEquals(response.get(0).getPlane().getPlaneCompanyName(), "testName");
        assertEquals(response.get(0).getPrice(), 200);
    }

}