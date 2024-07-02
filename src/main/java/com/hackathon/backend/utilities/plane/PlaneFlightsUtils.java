package com.hackathon.backend.utilities.plane;

import com.hackathon.backend.dto.planeDto.EditFlightDto;
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

    public List<PlaneFlightsEntity> findAllByDepartureAirPortIdAndDestinationAirPortId
            (int departurePlaceId, int destinationPlaceId) {
        return planeFlightsRepository
                .findAllByDepartureAirPortIdAndDestinationAirPortId
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

    public boolean checkHelper(EditFlightDto editFlightDto){
        return  editFlightDto.getPrice() != null ||
                editFlightDto.getDepartureTime() != null ||
                editFlightDto.getArrivalTime() != null ||
                editFlightDto.getDepartureAirPort() != null ||
                editFlightDto.getDestinationAirPort() != null;
    }

    public void editHelper(PlaneFlightsEntity planeFlights,
                           EditFlightDto editFlightDto) {
        if (editFlightDto.getPrice() != null && editFlightDto.getPrice() > 0) {
            planeFlights.setPrice(editFlightDto.getPrice());
        }
        if (editFlightDto.getDepartureTime() != null) {
            planeFlights.setDepartureTime(editFlightDto.getDepartureTime());
        }
        if (editFlightDto.getArrivalTime() != null) {
            planeFlights.setArrivalTime(editFlightDto.getArrivalTime());
        }
        if (editFlightDto.getDepartureAirPort() != null) {
            AirPortEntity airPortEntity = airPortsUtils.findAirPortByAirPort(editFlightDto.getDepartureAirPort());
            planeFlights.setDepartureAirPort(airPortEntity);
        }

        if (editFlightDto.getDestinationAirPort() != null) {
            AirPortEntity airPortEntity = airPortsUtils.findAirPortByAirPort(editFlightDto.getDestinationAirPort());
            planeFlights.setDestinationAirPort(airPortEntity);
        }
    }
}
