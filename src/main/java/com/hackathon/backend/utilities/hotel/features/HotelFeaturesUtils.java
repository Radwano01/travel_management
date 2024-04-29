package com.hackathon.backend.utilities.hotel.features;

import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.repositories.hotel.hotelFeatures.HotelFeaturesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HotelFeaturesUtils {

    private final HotelFeaturesRepository hotelFeaturesRepository;

    @Autowired
    public HotelFeaturesUtils(HotelFeaturesRepository hotelFeaturesRepository) {
        this.hotelFeaturesRepository = hotelFeaturesRepository;
    }

    public HotelFeaturesEntity findById(int featureId) {
        return hotelFeaturesRepository.findById(featureId)
                .orElseThrow(()-> new EntityNotFoundException("Feature id not found"));
    }

    public void save(HotelFeaturesEntity hotelFeatures) {
        hotelFeaturesRepository.save(hotelFeatures);
    }

    public boolean existsHotelFeatureByHotelFeatures(String feature) {
        return hotelFeaturesRepository.existsHotelFeatureByHotelFeatures(feature);
    }

    public List<HotelFeaturesEntity> findAll() {
        return hotelFeaturesRepository.findAll();
    }

    public void deleteById(int featureId) {
        hotelFeaturesRepository.deleteById(featureId);
    }
}
