package com.hackathon.backend.plane.repositories;

import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.repositories.plane.AirPortRepository;
import com.hackathon.backend.repositories.plane.PlaneFlightsRepository;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    long departureId;
    long destinationId;

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
        departureId = airPortEntity1.getId();

        AirPortEntity airPortEntity2 = new AirPortEntity(
                "airport two",
                "QWE",
                place2
        );
        airPortRepository.save(airPortEntity2);
        destinationId = airPortEntity2.getId();

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
                "2024-10-01T20:00:00",
                "2024-10-01T23:00:00",
                50
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

        //when
        List<FlightDto> response = planeFlightsRepository
                .findAllByDepartureAirPortIdAndDestinationAirPortId(departureId, destinationId);

        //then
        assertEquals(response.get(0).getDepartureAirPort(), "test1");
        assertEquals(response.get(0).getDestinationAirPort(), "test2");
        assertEquals(response.get(0).getDepartureAirPort(), "airport one");
        assertEquals(response.get(0).getDestinationAirPort(), "airport two");
        assertEquals(response.get(0).getPlaneCompanyName(), "testName");
        assertEquals(response.get(0).getPrice(), 200);
    }

}