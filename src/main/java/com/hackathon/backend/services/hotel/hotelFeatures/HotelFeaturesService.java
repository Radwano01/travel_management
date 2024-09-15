package com.hackathon.backend.services.hotel.hotelFeatures;

import com.hackathon.backend.dto.hotelDto.features.GetHotelFeaturesDto;
import com.hackathon.backend.dto.hotelDto.features.HotelFeatureDto;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HotelFeaturesService {

    ResponseEntity<String> createHotelFeature(HotelFeatureDto hotelFeatureDto);

    ResponseEntity<List<GetHotelFeaturesDto>> getHotelFeatures();

    ResponseEntity<String> editHotelFeature(int featureId, HotelFeatureDto hotelFeatureDto);

    ResponseEntity<String> deleteHotelFeature(int featureId);
}
