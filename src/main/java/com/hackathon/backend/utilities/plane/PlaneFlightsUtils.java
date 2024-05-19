package com.hackathon.backend.utilities.plane;

import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.plane.PlaneFlightsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PlaneFlightsUtils {

    private final PlaneFlightsRepository planeFlightsRepository;

    @Autowired
    public PlaneFlightsUtils(PlaneFlightsRepository planeFlightsRepository) {
        this.planeFlightsRepository = planeFlightsRepository;
    }

    public void save(PlaneFlightsEntity planeFlights) {
        planeFlightsRepository.save(planeFlights);
    }

    public List<PlaneFlightsEntity> findAllByDepartureCountryIdAndDestinationCountryId
            (int departureCountryId, int destinationCountryId) {
        return planeFlightsRepository
                .findAllByDepartureCountryIdAndDestinationCountryId
                        (departureCountryId,destinationCountryId);
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
}
