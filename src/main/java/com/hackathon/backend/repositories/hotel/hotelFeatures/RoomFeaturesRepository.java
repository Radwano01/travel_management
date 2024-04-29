package com.hackathon.backend.repositories.hotel.hotelFeatures;

import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomFeaturesRepository extends JpaRepository<RoomFeaturesEntity, Integer> {
    boolean existsRoomFeatureByRoomFeatures(String feature);
}
