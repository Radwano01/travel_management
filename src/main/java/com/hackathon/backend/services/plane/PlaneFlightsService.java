package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.planeDto.EditFlightDto;
import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.dto.planeDto.GetFlightDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlaneFlightsService {

    ResponseEntity<String> addFlight(long planeId, long departureAirPortId,
                                     long destinationAirPortId, FlightDto flightDto);

    ResponseEntity<List<GetFlightDto>> getFlights(long departureAirPortId,
                                                  long destinationAirPortId,
                                                  int page, int size);

    ResponseEntity<String> editFlight(long flightId, EditFlightDto editFlightDto);

    ResponseEntity<String> deleteFlight(long flightId);
}
