package com.hackathon.backend.controllers.hotel.hotelFeatures;

import com.hackathon.backend.services.hotel.hotelFeatures.HotelFeaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class HotelFeaturesController {

    private final HotelFeaturesService hotelFeaturesService;

    @Autowired
    private HotelFeaturesController(HotelFeaturesService hotelFeaturesService){
        this.hotelFeaturesService = hotelFeaturesService;
    }

    @PostMapping(path = "${CREATE_HOTEL_FEATURE_PATH}")
    public ResponseEntity<String> createHotelFeature(@RequestParam("hotelFeature") String hotelFeature){
        return hotelFeaturesService.createHotelFeature(hotelFeature);
    }

    @GetMapping(path = "${GET_HOTEL_FEATURES_PATH}")
    public ResponseEntity<?> getRoomFeatures(){
        return hotelFeaturesService.getHotelFeatures();
    }

    @PutMapping(path = "${EDIT_HOTEL_FEATURE_PATH}")
    public ResponseEntity<String> editHotelFeature(@PathVariable("featureId") int featureId,
                                              String hotelFeature){
        return hotelFeaturesService.editHotelFeature(featureId,hotelFeature);
    }

    @DeleteMapping(path = "${DELETE_HOTEL_FEATURE_PATH}")
    public ResponseEntity<String> deleteHotelFeature(@PathVariable("featureId") int featureId){
        return hotelFeaturesService.deleteHotelFeature(featureId);
    }
}
