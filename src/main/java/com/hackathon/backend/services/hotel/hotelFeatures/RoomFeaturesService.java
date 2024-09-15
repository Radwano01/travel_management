package com.hackathon.backend.services.hotel.hotelFeatures;

import com.hackathon.backend.dto.hotelDto.features.GetRoomFeaturesDto;
import com.hackathon.backend.dto.hotelDto.features.RoomFeatureDto;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RoomFeaturesService {

    ResponseEntity<String> createRoomFeature(RoomFeatureDto roomFeatureDto);

    ResponseEntity<List<GetRoomFeaturesDto>> getRoomFeatures();

    ResponseEntity<String> editRoomFeature(int featureId, RoomFeatureDto roomFeatureDto);

    ResponseEntity<String> deleteRoomFeature(int featureId);
}
