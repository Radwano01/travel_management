package com.hackathon.backend.plane.repositories;

import com.hackathon.backend.dto.planeDto.GetFlightDto;
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
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
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
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
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
    void findAllByDepartureAirPortIdAndDestinationAirPortId(){

        //when
        List<GetFlightDto> response = planeFlightsRepository
                .findAllByDepartureAirPortIdAndDestinationAirPortId(departureId, destinationId, PageRequest.of(0, 3));

        //then
        assertEquals(1, response.size());
        assertEquals("testName", response.get(0).getPlaneCompanyName());
        assertEquals(200, response.get(0).getPrice());
        assertEquals("airport one", response.get(0).getDepartureAirPort());
        assertEquals("QWE", response.get(0).getDepartureAirPortCode());
        assertEquals("airport two", response.get(0).getDestinationAirPort());
        assertEquals("QWE", response.get(0).getDestinationAirPortCode());
        assertEquals(50, response.get(0).getAvailableSeats());
    }
}
