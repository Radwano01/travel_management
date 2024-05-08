package com.hackathon.backend.utilities.country;

import com.hackathon.backend.dto.countryDto.placeDto.EssentialPlaceDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.repositories.country.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlaceUtils {

    private final PlaceRepository placeRepository;

    public PlaceUtils(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public void save(PlaceEntity place) {
        placeRepository.save(place);
    }

    public PlaceEntity findById(int placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(()-> new EntityNotFoundException("Place id not found"));
    }

    public void delete(PlaceEntity place) {
        placeRepository.delete(place);
    }

    public void deleteAll() {
        placeRepository.deleteAll();
    }

    public List<EssentialPlaceDto> findPlacesByCountryId(int countryId) {
        return placeRepository.findPlacesByCountryId(countryId);
    }
}
