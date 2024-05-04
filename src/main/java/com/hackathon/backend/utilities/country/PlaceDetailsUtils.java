package com.hackathon.backend.utilities.country;

import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.repositories.country.PlaceDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlaceDetailsUtils {

    private final PlaceDetailsRepository placeDetailsRepository;

    @Autowired
    public PlaceDetailsUtils(PlaceDetailsRepository placeDetailsRepository) {
        this.placeDetailsRepository = placeDetailsRepository;
    }

    public void save(PlaceDetailsEntity placeDetails) {
        placeDetailsRepository.save(placeDetails);
    }

    public PlaceDetailsEntity findById(int placeDetailsId) {
        return placeDetailsRepository.findById(placeDetailsId)
                .orElseThrow(()-> new EntityNotFoundException("Place details id not found"));
    }

    public void deleteAll() {
        placeDetailsRepository.deleteAll();
    }
}
