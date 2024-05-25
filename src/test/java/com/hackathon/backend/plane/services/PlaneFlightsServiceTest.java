package com.hackathon.backend.plane.services;

import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.services.plane.PlaneFlightsService;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
import com.hackathon.backend.utilities.plane.PlaneUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaneFlightsServiceTest {

    @Mock
    PlaneFlightsUtils planeFlightsUtils;

    @Mock
    PlaneUtils planeUtils;

    @Mock
    CountryUtils countryUtils;

    @InjectMocks
    PlaneFlightsService planeFlightsService;

    @Test
    void addFlight() {
        //given
        long planeId = 1;
        int departureCountryId = 1;
        int destinationCountryId = 2;

        PlaneEntity plane = new PlaneEntity();
        plane.setId(planeId);

        CountryEntity departureCountry = new CountryEntity();
        departureCountry.setId(departureCountryId);

        CountryEntity destinationCountry = new CountryEntity();
        destinationCountry.setId(destinationCountryId);

        FlightDto flightDto = new FlightDto();
        flightDto.setPlaneCompanyName("testPlane");
        flightDto.setPrice(100);
        flightDto.setDepartureCountry("testCountry1");
        flightDto.setDestinationCountry("testCountry2");
        flightDto.setDepartureTime(LocalDateTime.now());
        flightDto.setArrivalTime(LocalDateTime.now().plusHours(2));


        //behavior
        when(planeUtils.findPlaneById(planeId)).thenReturn(plane);
        when(countryUtils.findCountryById(departureCountryId)).thenReturn(departureCountry);
        when(countryUtils.findCountryById(destinationCountryId)).thenReturn(destinationCountry);

        //when
        ResponseEntity<?> response = planeFlightsService.addFlight(
                planeId, departureCountryId,
                destinationCountryId, flightDto
        );

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(planeUtils).findPlaneById(planeId);
        verify(countryUtils).findCountryById(departureCountryId);
        verify(countryUtils).findCountryById(destinationCountryId);
    }

    @Test
    void getFlights() {
        //given
        int departureCountryId = 1;
        int destinationCountryId = 2;

        CountryEntity country1 = new CountryEntity();
        country1.setId(departureCountryId);
        country1.setCountry("testCountry1");

        CountryEntity country2 = new CountryEntity();
        country2.setId(destinationCountryId);
        country2.setCountry("testCountry2");

        PlaneEntity plane = new PlaneEntity();
        plane.setPlaneCompanyName("testPlane");
        plane.setNumSeats(50);

        PlaneFlightsEntity flight1 = new PlaneFlightsEntity();
        flight1.setId(1);
        flight1.setPrice(100);
        flight1.setDepartureTime(LocalDateTime.now());
        flight1.setArrivalTime(LocalDateTime.now().plusHours(2));
        flight1.setPlane(plane);
        flight1.setDepartureCountry(country1);
        flight1.setDestinationCountry(country2);

        List<PlaneFlightsEntity> planeFlightsList = new ArrayList<>();
        planeFlightsList.add(flight1);

        //behavior
        when(planeFlightsUtils.findAllByDepartureCountryIdAndDestinationCountryId(departureCountryId, destinationCountryId))
                .thenReturn(planeFlightsList);

        //when
        ResponseEntity<?> response = planeFlightsService.getFlights(departureCountryId, destinationCountryId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightDto> flights = (List<FlightDto>) response.getBody();
        assertNotNull(flights);
        assertEquals(flight1.getId(), flights.get(0).getId());
        assertEquals(flight1.getPrice(), flights.get(0).getPrice());
        assertEquals(flight1.getPlane().getPlaneCompanyName(), flights.get(0).getPlaneCompanyName());
        assertEquals(flight1.getDepartureCountry().getCountry(), flights.get(0).getDepartureCountry());
        assertEquals(flight1.getDestinationCountry().getCountry(), flights.get(0).getDestinationCountry());
    }

    @Test
    void editFlight() {
        //given
        long flightId = 1L;
        int departureCountryId = 1;
        int destinationCountryId = 2;

        CountryEntity country1 = new CountryEntity();
        country1.setId(departureCountryId);
        country1.setCountry("testCountry1");

        CountryEntity country2 = new CountryEntity();
        country2.setId(destinationCountryId);
        country2.setCountry("testCountry2");

        PlaneFlightsEntity flight1 = new PlaneFlightsEntity();
        flight1.setId(1);
        flight1.setPrice(100);
        flight1.setDepartureTime(LocalDateTime.now());
        flight1.setArrivalTime(LocalDateTime.now().plusHours(2));
        flight1.setDepartureCountry(country1);
        flight1.setDestinationCountry(country2);

        FlightDto flightDto = new FlightDto();
        flightDto.setPrice(150);
        flightDto.setDepartureCountry("testCountry1");
        flightDto.setDestinationCountry("testCountry2");
        flightDto.setDepartureTime(LocalDateTime.now());
        flightDto.setArrivalTime(LocalDateTime.now().plusHours(2));

        //behavior
        when(planeFlightsUtils.findById(flightId)).thenReturn(flight1);

        //when
        ResponseEntity<?> response = planeFlightsService.editFlight(flightId, departureCountryId, destinationCountryId, flightDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flightDto.getPrice(), flight1.getPrice());
        assertEquals(flightDto.getDepartureCountry(), flight1.getDepartureCountry().getCountry());
        assertEquals(flightDto.getDestinationCountry(), flight1.getDestinationCountry().getCountry());
        assertEquals(flightDto.getDepartureTime(), flight1.getDepartureTime());
        assertEquals(flightDto.getArrivalTime(), flight1.getArrivalTime());
        verify(planeFlightsUtils).findById(flightId);
        verify(planeFlightsUtils).save(flight1);
    }

    @Test
    void deleteFlight() {
        //given
        long flightId = 1L;

        PlaneFlightsEntity planeFlights = new PlaneFlightsEntity();
        planeFlights.setId(flightId);

        //behavior
        when(planeFlightsUtils.findById(flightId)).thenReturn(planeFlights);

        //when
        ResponseEntity<?> response = planeFlightsService.deleteFlight(flightId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(planeFlightsUtils).findById(flightId);
        verify(planeFlightsUtils).deleteById(flightId);
    }
}