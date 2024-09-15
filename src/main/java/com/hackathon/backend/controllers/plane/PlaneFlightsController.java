package com.hackathon.backend.controllers.plane;

import com.hackathon.backend.dto.planeDto.EditFlightDto;
import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.services.plane.impl.PlaneFlightsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PlaneFlightsController {

    private final PlaneFlightsServiceImpl planeFlightsServiceImpl;

    @Autowired
    public PlaneFlightsController(PlaneFlightsServiceImpl planeFlightsServiceImpl) {
        this.planeFlightsServiceImpl = planeFlightsServiceImpl;
    }

    @PostMapping(path = "${ADD_FLIGHT_PATH}")
    public ResponseEntity<String> addFlight(@PathVariable("planeId") long planeId,
                                            @PathVariable("departureAirPortId") int departureAirPortId,
                                            @PathVariable("destinationAirPortId") int destinationAirPortId,
                                            @RequestBody FlightDto flightDto){
        try {
            return planeFlightsServiceImpl.addFlight(planeId, departureAirPortId, destinationAirPortId, flightDto);
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_FLIGHT_PATH}")
    public ResponseEntity<?> getFlight(@PathVariable("departureAirPortId") long departureAirPortId,
                                       @PathVariable("destinationAirPortId") long destinationAirPortId,
                                       @RequestParam("page") int page,
                                       @RequestParam("size") int size){
        try {
            return planeFlightsServiceImpl.getFlights(departureAirPortId, destinationAirPortId, page, size);
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_FLIGHTS_BY_FILTER_PATH}")
    public ResponseEntity<?> getFlights(@RequestParam(value = "page") int page,
                                        @RequestParam(value = "size") int size,
                                        @RequestParam(value = "departureAirPortId", required = false) Long departureAirPortId,
                                        @RequestParam(value = "destinationAirPortId", required = false) Long destinationAirPortId,
                                        @RequestParam(value = "planeCompanyName", required = false) String planeCompanyName){
        try{
            return planeFlightsServiceImpl.getFlights(page, size, departureAirPortId, destinationAirPortId, planeCompanyName);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_FLIGHT_PATH}")
    public ResponseEntity<String> editFlight(@PathVariable("flightId") long flightId,
                                             @RequestBody EditFlightDto editFlightDto){
        try {
            return planeFlightsServiceImpl.editFlight(flightId, editFlightDto);
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_FLIGHT_PATH}")
    public ResponseEntity<String> deleteFlight(@PathVariable("flightId") long flightId){
        try {
            return planeFlightsServiceImpl.deleteFlight(flightId);
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}
