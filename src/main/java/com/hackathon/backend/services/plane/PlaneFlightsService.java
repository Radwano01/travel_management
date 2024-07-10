package com.hackathon.backend.services.plane;

import com.hackathon.backend.dto.planeDto.EditFlightDto;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public ResponseEntity<String> addFlight(long planeId, long departureAirPortId,
                                       long destinationAirPortId, FlightDto flightDto) {
        try {
            PlaneEntity plane = planeUtils.findPlaneById(planeId);
            if(plane.getNumSeats() < flightDto.getAvailableSeats()){
                return badRequestException("the provided seats number is higher then plane seats");
            }
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
                    flightDto.getArrivalTime(),
                    flightDto.getAvailableSeats()
            );
            planeFlightsUtils.save(planeFlights);
            return ResponseEntity.ok("Flight added successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getFlights(int departureAirPortId,
                                        int destinationAirPortId) {
        try {
            List<FlightDto> planeFlights = planeFlightsUtils
                    .findAllByDepartureAirPortIdAndDestinationAirPortId(
                            departureAirPortId, destinationAirPortId
                    );

            List<FlightDto> flightDtoList = new ArrayList<>();
            for(FlightDto flights:planeFlights){
                    if(flights.getAvailableSeats() != 0) {
                        FlightDto flightDto = new FlightDto(
                                flights.getId(),
                                flights.getPlaneCompanyName(),
                                flights.getPrice(),
                                flights.getDepartureAirPort(),
                                flights.getDepartureAirPortCode(),
                                flights.getDestinationAirPort(),
                                flights.getDestinationAirPortCode(),
                                flights.getDepartureTime(),
                                flights.getArrivalTime(),
                                flights.getAvailableSeats()
                        );
                        flightDtoList.add(flightDto);
                    }
            }
            return ResponseEntity.ok(flightDtoList);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> editFlight(long flightId,
                                            EditFlightDto editFlightDto) {
        try {
            if(!planeFlightsUtils.checkHelper(editFlightDto)){
                return badRequestException("you sent an empty data to change");
            }
            PlaneFlightsEntity planeFlights = planeFlightsUtils.findById(flightId);
            planeFlightsUtils.editHelper(planeFlights, editFlightDto);
            planeFlightsUtils.save(planeFlights);

            return ResponseEntity.ok("Flight edit successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    public ResponseEntity<String> deleteFlight(long flightId) {
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


    @Scheduled(fixedRate = 60000)
    @Transactional
    public void removeFlights() {
        List<PlaneFlightsEntity> flights = planeFlightsUtils.findAll();

        LocalDateTime currentDateTime = LocalDateTime.now();

        for (PlaneFlightsEntity flight : flights) {
            if (flight.getAvailableSeats() == 0) {
                planeFlightsUtils.deleteById(flight.getId());
                continue;
            }

            LocalDateTime endTime = LocalDateTime.parse(flight.getArrivalTime());
            if (currentDateTime.isAfter(endTime)) {
                planeFlightsUtils.deleteById(flight.getId());
            }
        }
    }
}