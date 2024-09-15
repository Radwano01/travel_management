package com.hackathon.backend.plane.services;

import com.hackathon.backend.dto.planeDto.EditFlightDto;
import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.dto.planeDto.GetFlightDto;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.plane.AirPortRepository;
import com.hackathon.backend.repositories.plane.PlaneFlightsRepository;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import com.hackathon.backend.services.plane.impl.PlaneFlightsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaneFlightsServiceImplTest {

    @Mock
    PlaneRepository planeRepository;

    @Mock
    AirPortRepository airPortRepository;

    @Mock
    PlaneFlightsRepository planeFlightsRepository;

    @InjectMocks
    PlaneFlightsServiceImpl planeFlightsServiceImpl;

    @Test
    void addFlight_ShouldReturnFlight_WhenFlightIsAdded() {
        // given
        long planeId = 1L;
        long departureAirPortId = 1L;
        long destinationAirPortId = 2L;
        FlightDto flightDto = new FlightDto();
        flightDto.setPrice(100);
        flightDto.setDepartureTime(LocalDateTime.now().plusHours(1));
        flightDto.setArrivalTime(LocalDateTime.now().plusHours(2));

        PlaneEntity plane = new PlaneEntity();
        AirPortEntity departureAirport = new AirPortEntity();
        AirPortEntity destinationAirport = new AirPortEntity();

        // behavior
        when(planeRepository.findById(planeId)).thenReturn(Optional.of(plane));
        when(airPortRepository.findById(departureAirPortId)).thenReturn(Optional.of(departureAirport));
        when(airPortRepository.findById(destinationAirPortId)).thenReturn(Optional.of(destinationAirport));

        // when
        ResponseEntity<String> response = planeFlightsServiceImpl.addFlight(planeId, departureAirPortId, destinationAirPortId, flightDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("PlaneFlightsEntity"));
        verify(planeRepository).save(plane);
    }

    @Test
    void getFlights_ShouldReturnFlights_WhenFlightsExist() {
        // given
        long departureAirPortId = 1L;
        long destinationAirPortId = 2L;
        int page = 0;
        int size = 10;

        List<GetFlightDto> flightDtos = List.of(new GetFlightDto());

        // Mock dependencies
        when(planeFlightsRepository.findAllByDepartureAirPortIdAndDestinationAirPortId(
                departureAirPortId, destinationAirPortId, PageRequest.of(page, size)))
                .thenReturn(flightDtos);

        // when
        ResponseEntity<List<GetFlightDto>> response = planeFlightsServiceImpl.getFlights(departureAirPortId, destinationAirPortId, page, size);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flightDtos, response.getBody());
    }

    @Test
    void getFlights() {
        // given
        int page = 0;
        int size = 10;
        Long departureAirPortId = 1L;
        Long destinationAirPortId = 2L;
        String planeCompanyName = "Airline A";

        // Create mock flight DTOs with full details
        List<GetFlightDto> mockFlights = Arrays.asList(
                new GetFlightDto(
                        1L,
                        "Airline A",
                        500,
                        "JFK International",
                        "JFK",
                        "LAX International",
                        "LAX",
                        LocalDateTime.of(2023, 9, 14, 10, 30),
                        LocalDateTime.of(2023, 9, 14, 14, 30)
                ),
                new GetFlightDto(
                        2L,
                        "Airline A",
                        600,
                        "JFK International",
                        "JFK",
                        "LAX International",
                        "LAX",
                        LocalDateTime.of(2023, 9, 14, 12, 30),
                        LocalDateTime.of(2023, 9, 14, 16, 30)
                )
        );

        // behavior
        when(planeFlightsRepository.findAllByFilters(PageRequest.of(page, size), departureAirPortId, destinationAirPortId, planeCompanyName))
                .thenReturn(mockFlights);

        // when
        ResponseEntity<List<GetFlightDto>> response = planeFlightsServiceImpl.getFlights(page, size, departureAirPortId, destinationAirPortId, planeCompanyName);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals(planeCompanyName, response.getBody().get(0).getPlaneCompanyName());
        assertEquals("JFK", response.getBody().get(0).getDepartureAirPortCode());
        assertEquals("LAX", response.getBody().get(0).getDestinationAirPortCode());
        assertEquals(LocalDateTime.of(2023, 9, 14, 10, 30), response.getBody().get(0).getDepartureTime());
    }




    @Test
    void editFlight_ShouldReturnUpdatedFlight_WhenFlightIsEdited() {
        // given
        long flightId = 1L;
        EditFlightDto editFlightDto = new EditFlightDto();
        editFlightDto.setPrice(150);
        editFlightDto.setDepartureTime(LocalDateTime.now().plusHours(3));
        editFlightDto.setArrivalTime(LocalDateTime.now().plusHours(4));
        editFlightDto.setDepartureAirPortId(1L);
        editFlightDto.setDestinationAirPortId(2L);

        AirPortEntity departureAirPort = new AirPortEntity();
        departureAirPort.setId(2L);
        AirPortEntity destinationAirPort = new AirPortEntity();
        destinationAirPort.setId(1L);
        PlaneFlightsEntity existingFlight = new PlaneFlightsEntity();
        existingFlight.setId(flightId);
        existingFlight.setDepartureAirPort(departureAirPort);
        existingFlight.setDestinationAirPort(destinationAirPort);

        //behavior
        when(planeFlightsRepository.findById(flightId)).thenReturn(Optional.of(existingFlight));
        when(airPortRepository.findById(1L)).thenReturn(Optional.of(departureAirPort));
        when(airPortRepository.findById(2L)).thenReturn(Optional.of(destinationAirPort));

        // when
        ResponseEntity<String> updatedFlight = planeFlightsServiceImpl.editFlight(flightId, editFlightDto);

        // then
        assertNotNull(updatedFlight);
    }

    @Test
    void deleteFlight_ShouldReturnSuccess_WhenFlightIsDeleted() {
        // given
        long flightId = 1L;
        PlaneFlightsEntity flight = new PlaneFlightsEntity();
        PlaneEntity plane = new PlaneEntity();
        flight.setPlane(plane);

        // behavior
        when(planeFlightsRepository.findById(flightId)).thenReturn(Optional.of(flight));

        // when
        ResponseEntity<String> response = planeFlightsServiceImpl.deleteFlight(flightId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("flight deleted successfully", response.getBody());
        verify(planeFlightsRepository).delete(flight);
        verify(planeRepository).save(plane);
    }

    @Test
    void removeFlights_ShouldRemovePastFlights() {
        // given
        PlaneFlightsEntity pastFlight = new PlaneFlightsEntity();
        pastFlight.setArrivalTime(LocalDateTime.now().minusDays(1));
        PlaneEntity plane = new PlaneEntity();
        pastFlight.setPlane(plane);

        List<PlaneFlightsEntity> flights = List.of(pastFlight);

        // Mock dependencies
        when(planeFlightsRepository.findAll()).thenReturn(flights);
        doNothing().when(planeFlightsRepository).deleteById(pastFlight.getId());

        // when
        planeFlightsServiceImpl.removeFlights();

        // then
        verify(planeFlightsRepository).deleteById(pastFlight.getId());
        verify(planeRepository).save(plane);
    }

}