package com.hackathon.backend.plane.repositories;

import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.repositories.plane.AirPortRepository;
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
    AirPortRepository airPortRepository;

    @Autowired
    PlaneRepository planeRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    PlaneFlightsRepository planeFlightsRepository;

    @BeforeEach
    void setUp(){
        PlaceEntity place1 = new PlaceEntity();
        place1.setPlace("test1");
        placeRepository.save(place1);

        PlaceEntity place2 = new PlaceEntity();
        place2.setPlace("test2");
        placeRepository.save(place2);

        AirPortEntity airPortEntity1 = new AirPortEntity(
                "airport one",
                "QWE",
                place1
        );
        airPortRepository.save(airPortEntity1);

        AirPortEntity airPortEntity2 = new AirPortEntity(
                "airport two",
                "QWE",
                place2
        );
        airPortRepository.save(airPortEntity2);

        PlaneEntity plane = new PlaneEntity(
                "testName",
                100
        );
        planeRepository.save(plane);

        PlaneFlightsEntity planeFlights = new PlaneFlightsEntity(
                200,
                plane,
                airPortEntity1,
                airPortEntity2,
                "2024:10:01T20:00:00",
                "2024:10:01T23:00:00"
        );
        planeFlightsRepository.save(planeFlights);
    }

    @AfterEach
    void tearDown(){
        planeFlightsRepository.deleteAll();
        planeRepository.deleteAll();
        airPortRepository.deleteAll();
        placeRepository.deleteAll();
    }

    @Test
    void findAllByDepartureCountryIdAndDestinationCountryId(){
        //given
        int departureId = airPortRepository.findAll().get(0).getPlace().getId();
        int destinationId = airPortRepository.findAll().get(1).getPlace().getId();

        //when
        List<PlaneFlightsEntity> response = planeFlightsRepository
                .findAllByDeparturePlaceIdAndDestinationPlaceId(departureId,destinationId);

        //then
        assertEquals(response.get(0).getDepartureAirPort().getPlace().getPlace(), "test1");
        assertEquals(response.get(0).getDestinationAirPort().getPlace().getPlace(), "test2");
        assertEquals(response.get(0).getDepartureAirPort().getAirPortName(), "airport one");
        assertEquals(response.get(0).getDestinationAirPort().getAirPortName(), "airport two");
        assertEquals(response.get(0).getPlane().getPlaneCompanyName(), "testName");
        assertEquals(response.get(0).getPrice(), 200);
    }

}