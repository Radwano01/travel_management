package com.hackathon.backend.services.hotel.hotelFeatures;

import org.springframework.http.ResponseEntity;

public interface RoomFeaturesRelationsService {

    ResponseEntity<String> addRoomFeatureToHotel(long hotelId, int featureId);

    ResponseEntity<String> removeRoomFeatureFromHotel(long hotelId, int featureId);
}
