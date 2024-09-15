package com.hackathon.backend.plane.repositories;

import com.hackathon.backend.dto.planeDto.GetFlightDto;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.repositories.plane.AirPortRepository;
import com.hackathon.backend.repositories.plane.PlaneFlightsRepository;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlaneFlightsRepositoryTest {

    @Autowired
    PlaneFlightsRepository planeFlightsRepository;

    @Autowired
    PlaneRepository planeRepository;

    @Autowired
    AirPortRepository airPortRepository;

    @BeforeEach
    void setUp() {
        // Clean up any existing data to avoid conflicts
        planeFlightsRepository.deleteAll();
        planeRepository.deleteAll();
        airPortRepository.deleteAll();

        // Create and save airports
        AirPortEntity departureAirport = new AirPortEntity();
        departureAirport.setAirPortName("JFK");
        departureAirport.setAirPortCode("JFK");
        departureAirport = airPortRepository.save(departureAirport);

        AirPortEntity destinationAirport = new AirPortEntity();
        destinationAirport.setAirPortName("LAX");
        destinationAirport.setAirPortCode("LAX");
        destinationAirport = airPortRepository.save(destinationAirport);

        // Create and save a plane
        PlaneEntity plane = new PlaneEntity();
        plane.setPlaneCompanyName("Airways");
        plane.setNumSeats(150);
        plane.setPaidSeats(0);
        plane.setStatus(true);
        plane = planeRepository.save(plane);

        // Create and save flights
        PlaneFlightsEntity flight1 = new PlaneFlightsEntity();
        flight1.setPlane(plane);
        flight1.setPrice(500);
        flight1.setDepartureAirPort(departureAirport);
        flight1.setDestinationAirPort(destinationAirport);
        flight1.setDepartureTime(LocalDateTime.of(2024, 8, 25, 10, 0));
        flight1.setArrivalTime(LocalDateTime.of(2024, 8, 25, 13, 0));
        planeFlightsRepository.save(flight1);
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        planeFlightsRepository.deleteAll();
        planeRepository.deleteAll();
        airPortRepository.deleteAll();
    }

    @Test
    void findAllByDepartureAirPortIdAndDestinationAirPortId() {
        //given
        long departureAirPortId = 1L;
        long destinationAirPortId = 2L;
        Pageable pageable = PageRequest.of(0, 10);

        //when
        List<GetFlightDto> flights = planeFlightsRepository.findAllByDepartureAirPortIdAndDestinationAirPortId(
                departureAirPortId,
                destinationAirPortId,
                pageable
        );

        //then
        assertNotNull(flights);

        GetFlightDto flightDto1 = flights.get(0);
        assertEquals("Airways", flightDto1.getPlaneCompanyName());
        assertEquals(500, flightDto1.getPrice());
        assertEquals("JFK", flightDto1.getDepartureAirPortCode());
        assertEquals("LAX", flightDto1.getDestinationAirPortCode());
    }

    @Test
    void findAllByFilters() {
        // given
        Long departureAirPortId = 1L;
        Long destinationAirPortId = 2L;
        String planeCompanyName = "Airways";
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<GetFlightDto> flights = planeFlightsRepository.findAllByFilters(
                pageable,
                departureAirPortId,
                destinationAirPortId,
                planeCompanyName
        );

        // then
        assertNotNull(flights);
        assertFalse(flights.isEmpty());

        GetFlightDto flightDto = flights.get(0);
        assertEquals("Airways", flightDto.getPlaneCompanyName());
        assertEquals(500, flightDto.getPrice());
        assertEquals("JFK", flightDto.getDepartureAirPortCode());
        assertEquals("LAX", flightDto.getDestinationAirPortCode());
    }
}
