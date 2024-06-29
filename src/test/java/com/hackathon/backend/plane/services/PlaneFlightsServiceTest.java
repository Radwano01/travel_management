package com.hackathon.backend.plane.services;

import com.hackathon.backend.dto.planeDto.EditFlightDto;
import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.services.plane.PlaneFlightsService;
import com.hackathon.backend.utilities.plane.AirPortsUtils;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
import com.hackathon.backend.utilities.plane.PlaneUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaneFlightsServiceTest {

    @Mock
    PlaneFlightsUtils planeFlightsUtils;

    @Mock
    PlaneUtils planeUtils;

    @Mock
    AirPortsUtils airPortsUtils;

    @InjectMocks
    PlaneFlightsService planeFlightsService;

    @Test
    void addFlight() {
        //given
        long planeId = 1;
        long departureAirPortId = 1;
        long destinationAirPortId = 2;

        PlaneEntity plane = new PlaneEntity();
        plane.setId(planeId);

        AirPortEntity departureAirPort = new AirPortEntity();
        departureAirPort.setId(departureAirPortId);

        AirPortEntity destinationAirPort = new AirPortEntity();
        destinationAirPort.setId(destinationAirPortId);

        FlightDto flightDto = new FlightDto();
        flightDto.setPlaneCompanyName("testPlane");
        flightDto.setPrice(100);
        flightDto.setDepartureAirPort("departureAirport");
        flightDto.setDestinationAirPort("destinationAirport");
        flightDto.setDepartureTime("2024/10/01T20:00:00");
        flightDto.setArrivalTime("2024/10/01T22:00:00");

        //behavior
        when(planeUtils.findPlaneById(planeId)).thenReturn(plane);
        when(airPortsUtils.findById(departureAirPortId)).thenReturn(departureAirPort);
        when(airPortsUtils.findById(destinationAirPortId)).thenReturn(destinationAirPort);

        //when
        ResponseEntity<?> response = planeFlightsService.addFlight(
                planeId, departureAirPortId, destinationAirPortId, flightDto
        );

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(planeUtils).findPlaneById(planeId);
        verify(airPortsUtils).findById(departureAirPortId);
        verify(airPortsUtils).findById(destinationAirPortId);
    }

    @Test
    void getFlights() {
        //given
        int departureAirPortId = 1;
        int destinationAirPortId = 2;

        AirPortEntity departureAirPort = new AirPortEntity();
        departureAirPort.setId(departureAirPortId);
        departureAirPort.setAirPortName("departureAirport");

        AirPortEntity destinationAirPort = new AirPortEntity();
        destinationAirPort.setId(destinationAirPortId);
        destinationAirPort.setAirPortName("destinationAirport");

        PlaneEntity plane = new PlaneEntity();
        plane.setPlaneCompanyName("testPlane");

        PlaneFlightsEntity flight1 = new PlaneFlightsEntity();
        flight1.setId(1);
        flight1.setPrice(100);
        flight1.setDepartureAirPort(departureAirPort);
        flight1.setDestinationAirPort(destinationAirPort);
        flight1.setDepartureTime("2024/10/01T20:00:00");
        flight1.setArrivalTime("2024/10/01T22:00:00");
        flight1.setPlane(plane);

        List<PlaneFlightsEntity> planeFlightsList = new ArrayList<>();
        planeFlightsList.add(flight1);

        //behavior
        when(planeFlightsUtils.findAllByDeparturePlaceIdAndDestinationPlaceId(departureAirPortId, destinationAirPortId))
                .thenReturn(planeFlightsList);

        //when
        ResponseEntity<?> response = planeFlightsService.getFlights(departureAirPortId, destinationAirPortId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FlightDto> flights = (List<FlightDto>) response.getBody();
        assertNotNull(flights);
        assertEquals(flight1.getId(), flights.get(0).getId());
        assertEquals(flight1.getPrice(), flights.get(0).getPrice());
        assertEquals(flight1.getPlane().getPlaneCompanyName(), flights.get(0).getPlaneCompanyName());
        assertEquals(flight1.getDepartureAirPort().getAirPortName(), flights.get(0).getDepartureAirPort());
        assertEquals(flight1.getDestinationAirPort().getAirPortName(), flights.get(0).getDestinationAirPort());
    }

    @Test
    void editFlight() {
        // given
        long flightId = 1L;

        PlaneFlightsEntity flight1 = new PlaneFlightsEntity();
        flight1.setId(flightId);
        flight1.setPrice(100);
        flight1.setDepartureTime("2024/10/01T20:00:00");
        flight1.setArrivalTime("2024/10/01T22:00:00");

        EditFlightDto editFlightDto = new EditFlightDto();
        editFlightDto.setPrice(150);
        editFlightDto.setDepartureTime("2024/10/01T21:00:00");
        editFlightDto.setArrivalTime("2024/10/01T23:00:00");

        // behavior
        when(planeFlightsUtils.findById(flightId)).thenReturn(flight1);
        when(planeFlightsUtils.checkHelper(any(EditFlightDto.class))).thenReturn(true);

        doAnswer(invocation -> {
            PlaneFlightsEntity planeFlights = invocation.getArgument(0);
            EditFlightDto dto = invocation.getArgument(1);

            planeFlights.setPrice(dto.getPrice());
            planeFlights.setDepartureTime(dto.getDepartureTime());
            planeFlights.setArrivalTime(dto.getArrivalTime());

            return null;
        }).when(planeFlightsUtils).editHelper(any(PlaneFlightsEntity.class), any(EditFlightDto.class));

        // when
        ResponseEntity<?> response = planeFlightsService.editFlight(flightId, editFlightDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(editFlightDto.getPrice(), flight1.getPrice());
        assertEquals(editFlightDto.getDepartureTime(), flight1.getDepartureTime());
        assertEquals(editFlightDto.getArrivalTime(), flight1.getArrivalTime());
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
