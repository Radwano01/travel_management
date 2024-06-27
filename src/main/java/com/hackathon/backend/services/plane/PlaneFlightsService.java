package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.utilities.plane.AirPortsUtils;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
import com.hackathon.backend.utilities.plane.PlaneUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PlaneFlightsService {

    private final PlaneFlightsUtils planeFlightsUtils;
    private final PlaneUtils planeUtils;
    private final AirPortsUtils airPortsUtils;

    @Autowired
    public PlaneFlightsService(PlaneFlightsUtils planeFlightsUtils,
                               PlaneUtils planeUtils,
                               AirPortsUtils airPortsUtils) {
        this.planeFlightsUtils = planeFlightsUtils;
        this.planeUtils = planeUtils;
        this.airPortsUtils = airPortsUtils;
    }

    public ResponseEntity<?> addFlight(long planeId, long departureAirPortId,
                                       long destinationAirPortId, FlightDto flightDto) {
        try {
            PlaneEntity plane = planeUtils.findPlaneById(planeId);
            if (plane.getFlight() != null) {
                return alreadyValidException("This plane has already flight");
            }
            AirPortEntity departureAirPort = airPortsUtils.findById(departureAirPortId);
            AirPortEntity destinationAirPort = airPortsUtils.findById(destinationAirPortId);
            PlaneFlightsEntity planeFlights = new PlaneFlightsEntity(
                    flightDto.getPrice(),
                    plane,
                    departureAirPort,
                    destinationAirPort,
                    flightDto.getDepartureTime(),
                    flightDto.getArrivalTime()
            );
            planeFlightsUtils.save(planeFlights);
            return ResponseEntity.ok("Flight added successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getFlights(int departurePlaceId,
                                        int destinationPlaceId) {
        try {
            List<PlaneFlightsEntity> planeFlights = planeFlightsUtils
                    .findAllByDeparturePlaceIdAndDestinationPlaceId(
                            departurePlaceId, destinationPlaceId
                    );
            List<FlightDto> flights = planeFlights.stream()
                    .map((flight) -> new FlightDto(
                            flight.getId(),
                            flight.getPlane().getPlaneCompanyName(),
                            flight.getPrice(),
                            flight.getDepartureAirPort().getAirPortName(),
                            flight.getDepartureAirPort().getAirPortCode(),
                            flight.getDestinationAirPort().getAirPortName(),
                            flight.getDestinationAirPort().getAirPortCode(),
                            flight.getDepartureTime(),
                            flight.getArrivalTime()
                    )).toList();
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editFlight(long flightId,
                                        FlightDto flightDto) {
        try {
            if(!planeFlightsUtils.checkHelper(flightDto)){
                return badRequestException("you sent an empty data to change");
            }
            PlaneFlightsEntity planeFlights = planeFlightsUtils.findById(flightId);
            planeFlightsUtils.editHelper(planeFlights, flightDto);
            planeFlightsUtils.save(planeFlights);
            return ResponseEntity.ok("Flight edit successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> deleteFlight(long flightId) {
        try {
            PlaneFlightsEntity planeFlights = planeFlightsUtils.findById(flightId);
            if (planeFlights != null) {
                planeFlightsUtils.deleteById(flightId);
                return ResponseEntity.ok("flight deleted successfully");
            } else {
                return notFoundException("flight not found");
            }
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}