package com.hackathon.backend.controllers.plane;

import com.hackathon.backend.dto.planeDto.EditFlightDto;
import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.services.plane.PlaneFlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PlaneFlightsController {

    private final PlaneFlightsService planeFlightsService;

    @Autowired
    public PlaneFlightsController(PlaneFlightsService planeFlightsService) {
        this.planeFlightsService = planeFlightsService;
    }

    @PostMapping(path = "${ADD_FLIGHT_PATH}")
    public ResponseEntity<?> addFlight(@PathVariable("planeId") long planeId,
                                       @PathVariable("departureCountryId") int departureCountryId,
                                       @PathVariable("destinationCountryId") int destinationCountryId,
                                       @RequestBody FlightDto flightDto){
        return planeFlightsService.addFlight(planeId,departureCountryId,destinationCountryId,flightDto);
    }

    @GetMapping(path = "${GET_FLIGHT_PATH}")
    public ResponseEntity<?> getFlight(@PathVariable("departurePlaceId") int departurePlaceId,
                                       @PathVariable("destinationPlaceId") int destinationPlaceId){
        return planeFlightsService.getFlights(departurePlaceId,destinationPlaceId);
    }

    @PutMapping(path = "${EDIT_FLIGHT_PATH}")
    public ResponseEntity<?> editFlight(@PathVariable("flightId") long flightId,
                                        @RequestBody EditFlightDto editFlightDto){
        return planeFlightsService.editFlight(
                flightId,
                editFlightDto
        );
    }

    @DeleteMapping(path = "${DELETE_FLIGHT_PATH}")
    public ResponseEntity<?> deleteFlight(@PathVariable("flightId") long flightId){
        return planeFlightsService.deleteFlight(flightId);
    }
}
