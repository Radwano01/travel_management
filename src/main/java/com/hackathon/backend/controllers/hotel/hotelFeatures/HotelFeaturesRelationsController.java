package com.hackathon.backend.controllers.hotel.hotelFeatures;

import com.hackathon.backend.services.hotel.hotelFeatures.HotelFeaturesRelationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class HotelFeaturesRelationsController {

    private final HotelFeaturesRelationsService hotelFeaturesRelationsService;

    @Autowired
    public HotelFeaturesRelationsController(HotelFeaturesRelationsService hotelFeaturesRelationsService) {
        this.hotelFeaturesRelationsService = hotelFeaturesRelationsService;
    }

    @PostMapping(path = "${HOTEL_FEATURE_RELATIONS_PATH}")
    public ResponseEntity<String> addHotelFeatureToHotel(@PathVariable("hotelId") long hotelId,
                                                    @PathVariable("featureId") int featureId){
        return hotelFeaturesRelationsService.addHotelFeatureToHotel(hotelId,featureId);
    }

    @DeleteMapping(path = "${HOTEL_FEATURE_RELATIONS_PATH}")
    public ResponseEntity<String> removeHotelFeatureToHotel(@PathVariable("hotelId") long hotelId,
                                                    @PathVariable("featureId") int featureId){
        return hotelFeaturesRelationsService.removeHotelFeatureFromHotel(hotelId,featureId);
    }
}
