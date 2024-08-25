package com.hackathon.backend.services.hotel.hotelFeatures;

import org.springframework.http.ResponseEntity;

public interface HotelFeaturesRelationsService {
    ResponseEntity<String> addHotelFeatureToHotel(long hotelId, int featureId);

    ResponseEntity<String> removeHotelFeatureFromHotel(long hotelId, int featureId);
}
