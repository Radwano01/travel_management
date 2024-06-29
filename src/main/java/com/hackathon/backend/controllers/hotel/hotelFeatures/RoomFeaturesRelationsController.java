package com.hackathon.backend.controllers.hotel.hotelFeatures;

import com.hackathon.backend.services.hotel.hotelFeatures.RoomFeaturesRelationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoomFeaturesRelationsController {

    private final RoomFeaturesRelationsService roomFeaturesRelationsService;

    @Autowired
    public RoomFeaturesRelationsController(RoomFeaturesRelationsService roomFeaturesRelationsService) {
        this.roomFeaturesRelationsService = roomFeaturesRelationsService;
    }

    @PostMapping(path = "${ROOM_FEATURE_RELATIONS_PATH}")
    public ResponseEntity<String> addHotelFeatureToHotel(@PathVariable("hotelId") long hotelId,
                                                    @PathVariable("featureId") int featureId){
        return roomFeaturesRelationsService.addRoomFeatureToHotel(hotelId,featureId);
    }

    @DeleteMapping(path = "${ROOM_FEATURE_RELATIONS_PATH}")
    public ResponseEntity<String> removeHotelFeatureToHotel(@PathVariable("hotelId") long hotelId,
                                                       @PathVariable("featureId") int featureId){
        return roomFeaturesRelationsService.removeRoomFeatureFromHotel(hotelId,featureId);
    }
}
