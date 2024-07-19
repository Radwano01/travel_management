package com.hackathon.backend.plane.services;

import com.hackathon.backend.dto.planeDto.EditFlightDto;
import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.dto.planeDto.GetFlightDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
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
        flightDto.setPrice(100);
        flightDto.setDepartureTime(LocalDateTime.now());
        flightDto.setArrivalTime(LocalDateTime.now().plusHours(2));

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
        // given
        int departureAirPortId = 1;
        int destinationAirPortId = 2;

        int page = 0;
        int size = 3;
        Pageable pageable = PageRequest.of(page, size);

        List<GetFlightDto> mockPlaneFlights = new ArrayList<>();
        mockPlaneFlights.add(new GetFlightDto(1, "TestPlane", 100, "DepartureAirport", "DEP", "DestinationAirport", "DEST", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 10));
        mockPlaneFlights.add(new GetFlightDto(2, "AnotherPlane", 120, "DepartureAirport", "DEP", "DestinationAirport", "DEST", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 5));

        //behavior
        when(planeFlightsUtils.findAllByDepartureAirPortIdAndDestinationAirPortId(departureAirPortId, destinationAirPortId, pageable))
                .thenReturn(mockPlaneFlights);

        // when
        ResponseEntity<?> response = planeFlightsService.getFlights(departureAirPortId, destinationAirPortId, page, size);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<GetFlightDto> flights = (List<GetFlightDto>) response.getBody();
        assertEquals(2, flights.size());

        assertEquals(mockPlaneFlights.get(0).getId(), flights.get(0).getId());
        assertEquals(mockPlaneFlights.get(0).getPrice(), flights.get(0).getPrice());
        assertEquals(mockPlaneFlights.get(0).getPlaneCompanyName(), flights.get(0).getPlaneCompanyName());
        assertEquals(mockPlaneFlights.get(0).getDepartureAirPort(), flights.get(0).getDepartureAirPort());
        assertEquals(mockPlaneFlights.get(0).getDestinationAirPort(), flights.get(0).getDestinationAirPort());

        assertEquals(mockPlaneFlights.get(1).getId(), flights.get(1).getId());
        assertEquals(mockPlaneFlights.get(1).getPrice(), flights.get(1).getPrice());
        assertEquals(mockPlaneFlights.get(1).getPlaneCompanyName(), flights.get(1).getPlaneCompanyName());
        assertEquals(mockPlaneFlights.get(1).getDepartureAirPort(), flights.get(1).getDepartureAirPort());
        assertEquals(mockPlaneFlights.get(1).getDestinationAirPort(), flights.get(1).getDestinationAirPort());

        verify(planeFlightsUtils, times(1))
                .findAllByDepartureAirPortIdAndDestinationAirPortId(departureAirPortId, destinationAirPortId, pageable);
        verifyNoMoreInteractions(planeFlightsUtils);
    }


    @Test
    void editFlight() {
        // given
        long flightId = 1L;

        PlaneFlightsEntity flight1 = new PlaneFlightsEntity();
        flight1.setId(flightId);
        flight1.setPrice(100);
        flight1.setDepartureTime(LocalDateTime.now());
        flight1.setArrivalTime(LocalDateTime.now().plusHours(2));

        EditFlightDto editFlightDto = new EditFlightDto();
        editFlightDto.setPrice(150);
        editFlightDto.setDepartureTime(LocalDateTime.now());
        editFlightDto.setArrivalTime(LocalDateTime.now().plusHours(2));

        // behavior
        when(planeFlightsUtils.findById(flightId)).thenReturn(flight1);
        when(planeFlightsUtils.checkHelper(any(EditFlightDto.class))).thenReturn(true);

        doAnswer(invocation -> {
            PlaneFlightsEntity planeFlights = invocation.getArgument(0);
            EditFlightDto dto = invocation.getArgument(1);

            planeFlights.setPrice(dto.getPrice());
            planeFlights.setDepartureTime(LocalDateTime.now());
            planeFlights.setArrivalTime(LocalDateTime.now().plusHours(2));

            return null;
        }).when(planeFlightsUtils).editHelper(any(PlaneFlightsEntity.class), any(EditFlightDto.class));

        // when
        ResponseEntity<?> response = planeFlightsService.editFlight(flightId, editFlightDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(editFlightDto.getPrice(), flight1.getPrice());
        verify(planeFlightsUtils).findById(flightId);
        verify(planeFlightsUtils).save(flight1);
    }

    @Test
    void deleteFlight() {
        //given
        long flightId = 1L;

        PlaneFlightsEntity planeFlights = new PlaneFlightsEntity();
        planeFlights.setId(flightId);
        planeFlights.setPlane(new PlaneEntity());

        //behavior
        when(planeFlightsUtils.findById(flightId)).thenReturn(planeFlights);

        //when
        ResponseEntity<?> response = planeFlightsService.deleteFlight(flightId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(planeFlightsUtils).findById(flightId);
        verify(planeFlightsUtils).delete(planeFlights);
    }
}
