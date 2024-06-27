package com.hackathon.backend.utilities.plane;

import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.plane.PlaneFlightsRepository;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PlaneFlightsUtils {

    private final PlaneFlightsRepository planeFlightsRepository;

    private final AirPortsUtils airPortsUtils;


    @Autowired
    public PlaneFlightsUtils(PlaneFlightsRepository planeFlightsRepository,
                             AirPortsUtils airPortsUtils) {
        this.planeFlightsRepository = planeFlightsRepository;
        this.airPortsUtils = airPortsUtils;
    }

    public void save(PlaneFlightsEntity planeFlights) {
        planeFlightsRepository.save(planeFlights);
    }

    public List<PlaneFlightsEntity> findAllByDeparturePlaceIdAndDestinationPlaceId
            (int departurePlaceId, int destinationPlaceId) {
        return planeFlightsRepository
                .findAllByDeparturePlaceIdAndDestinationPlaceId
                        (departurePlaceId,destinationPlaceId);
    }

    public PlaneFlightsEntity findById(long flightId) {
        return planeFlightsRepository.findById(flightId)
                .orElseThrow(()-> new EntityNotFoundException("flight not found"));
    }

    public void deleteById(long flightId) {
        planeFlightsRepository.deleteById(flightId);
    }

    public void delete(PlaneFlightsEntity flight) {
        planeFlightsRepository.delete(flight);
    }

    public boolean checkHelper(FlightDto flightDto){
        return  flightDto.getPrice() != null ||
                flightDto.getDepartureTime() != null ||
                flightDto.getArrivalTime() != null ||
                flightDto.getDepartureAirPort() != null ||
                flightDto.getDestinationAirPort() != null;
    }

    public void editHelper(PlaneFlightsEntity planeFlights,
                           FlightDto flightDto) {
        if (flightDto.getPrice() != null && flightDto.getPrice() > 0) {
            planeFlights.setPrice(flightDto.getPrice());
        }
        if (flightDto.getDepartureTime() != null) {
            planeFlights.setDepartureTime(flightDto.getDepartureTime());
        }
        if (flightDto.getArrivalTime() != null) {
            planeFlights.setArrivalTime(flightDto.getArrivalTime());
        }
        if (flightDto.getDepartureAirPort() != null) {
            AirPortEntity airPortEntity = airPortsUtils.findAirPortByAirPort(flightDto.getDepartureAirPort());
            planeFlights.setDepartureAirPort(airPortEntity);
        }

        if (flightDto.getDestinationAirPort() != null) {
            AirPortEntity airPortEntity = airPortsUtils.findAirPortByAirPort(flightDto.getDestinationAirPort());
            planeFlights.setDestinationAirPort(airPortEntity);
        }
    }
}
