package com.hackathon.backend.plane.services;

import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.services.plane.PlaneFlightsService;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
import com.hackathon.backend.utilities.plane.PlaneUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PlaneFlightsServiceTest {

    @Mock
    private PlaneFlightsUtils planeFlightsUtils;

    @Mock
    private PlaneUtils planeUtils;

    @Mock
    private CountryUtils countryUtils;

    private PlaneFlightsService planeFlightsService;

    @BeforeEach
    void setUp() {
        planeFlightsService = new PlaneFlightsService(
                planeFlightsUtils,
                planeUtils,
                countryUtils);
    }

    @AfterEach
    void tearDown() {
        planeFlightsUtils.deleteAll();
    }

    @Test
    void addFlight() {
        //given
        long planeId = 1L;
        int departureCountryId = 1;
        int destinationCountryId = 2;
        FlightDto flightDto = new FlightDto();
        flightDto.setPrice(100);
        flightDto.setDepartureTime(LocalDateTime.now());
        flightDto.setArrivalTime(LocalDateTime.now().plusHours(3));

        when(planeUtils.findPlaneById(planeId)).thenReturn(new PlaneEntity());
        when(countryUtils.findCountryById(departureCountryId)).thenReturn(new CountryEntity());
        when(countryUtils.findCountryById(departureCountryId)).thenReturn(new CountryEntity());

        //when
        ResponseEntity<?> response = planeFlightsService.addFlight(planeId,departureCountryId,
                destinationCountryId,flightDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getFlights() {
        //given
        int departureCountryId = 1;
        int destinationCountryId = 2;

        PlaneFlightsEntity planeFlights = new PlaneFlightsEntity();
        planeFlights.setPlane(new PlaneEntity());
        planeFlights.setDepartureCountry(new CountryEntity());
        planeFlights.setDestinationCountry(new CountryEntity());
        planeFlights.setId(1);
        planeFlights.setPrice(100);
        planeFlights.getPlane().setPlaneCompanyName("test company name");
        planeFlights.getDepartureCountry().setCountry("country One");
        planeFlights.getDestinationCountry().setCountry("country Two");
        planeFlights.setDepartureTime(LocalDateTime.now());
        planeFlights.setArrivalTime(LocalDateTime.now().plusHours(3));

        List<PlaneFlightsEntity> planeFlightsEntityList = new ArrayList<>();
        planeFlightsEntityList.add(planeFlights);
        when(planeFlightsUtils
                .findAllByDepartureCountryIdAndDestinationCountryId(departureCountryId,destinationCountryId))
                .thenReturn(planeFlightsEntityList);
        //when
        ResponseEntity<?> response = planeFlightsService.getFlights(departureCountryId,destinationCountryId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<FlightDto> flightDtos = (List<FlightDto>) response.getBody();
        assertEquals(planeFlights.getId(), flightDtos.get(0).getId());
        assertEquals(planeFlights.getPrice(), flightDtos.get(0).getPrice());
        assertEquals(planeFlights.getPlane().getPlaneCompanyName(), flightDtos.get(0).getPlaneCompanyName());
        assertEquals(planeFlights.getDepartureCountry().getCountry(), flightDtos.get(0).getDepartureCountry());
        assertEquals(planeFlights.getDestinationCountry().getCountry(), flightDtos.get(0).getDestinationCountry());
        assertEquals(planeFlights.getDepartureTime(), flightDtos.get(0).getDepartureTime());
        assertEquals(planeFlights.getArrivalTime(), flightDtos.get(0).getArrivalTime());
    }

    @Test
    void editFlight() {
        //given
        long flightId = 1;
        int departureCountryId = 1;
        int destinationCountryId = 1;

        CountryEntity country1 = new CountryEntity();
        country1.setId(departureCountryId);

        CountryEntity country2 = new CountryEntity();
        country2.setId(destinationCountryId);

        FlightDto flightDto = new FlightDto();
        flightDto.setPrice(100);
        flightDto.setDepartureTime(LocalDateTime.now());
        flightDto.setArrivalTime(LocalDateTime.now().plusHours(3));

        PlaneFlightsEntity planeFlights = new PlaneFlightsEntity();
        planeFlights.setId(flightId);
        planeFlights.setDepartureCountry(country1);
        planeFlights.setDestinationCountry(country2);

        when(planeFlightsUtils.findById(flightId))
                .thenReturn(planeFlights);

        //when
        ResponseEntity<?> response = planeFlightsService.editFlight(
                flightId, departureCountryId, destinationCountryId, flightDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteFlight() {
        //given
        long flightId = 1;

        when(planeFlightsUtils.findById(flightId)).thenReturn(new PlaneFlightsEntity());
        //when
        ResponseEntity<?> response = planeFlightsService.deleteFlight(flightId);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}