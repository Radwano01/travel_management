package com.hackathon.backend.repositories.hotel.hotelFeatures;

import com.hackathon.backend.dto.hotelDto.features.GetHotelFeaturesDto;
import com.hackathon.backend.dto.hotelDto.features.GetRoomFeaturesDto;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomFeaturesRepository extends JpaRepository<RoomFeaturesEntity, Integer> {
    boolean existsRoomFeatureByRoomFeatures(String feature);

    @Query("SELECT new com.hackathon.backend.dto.hotelDto.features.GetRoomFeaturesDto" +
            "(r.id, r.roomFeatures)" +
            " FROM RoomFeaturesEntity r")
    List<GetRoomFeaturesDto> findAllRoomFeatures();
}
