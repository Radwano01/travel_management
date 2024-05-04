package com.hackathon.backend.utilities.hotel.features;

import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.repositories.hotel.hotelFeatures.RoomFeaturesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoomFeaturesUtils {

    private final RoomFeaturesRepository roomFeaturesRepository;

    @Autowired
    public RoomFeaturesUtils(RoomFeaturesRepository roomFeaturesRepository) {
        this.roomFeaturesRepository = roomFeaturesRepository;
    }

    public boolean existsRoomFeatureByRoomFeatures(String feature) {
        return roomFeaturesRepository.existsRoomFeatureByRoomFeatures(feature);
    }

    public void save(RoomFeaturesEntity roomFeatures) {
        roomFeaturesRepository.save(roomFeatures);
    }

    public List<RoomFeaturesEntity> findAll() {
        return roomFeaturesRepository.findAll();
    }

    public RoomFeaturesEntity findById(int featureId) {
        return roomFeaturesRepository.findById(featureId)
                .orElseThrow(()-> new EntityNotFoundException("Room feature not found"));
    }

    public void deleteById(int featureId) {
        roomFeaturesRepository.deleteById(featureId);
    }

    public void deleteAll() {
        roomFeaturesRepository.deleteAll();
    }
}
