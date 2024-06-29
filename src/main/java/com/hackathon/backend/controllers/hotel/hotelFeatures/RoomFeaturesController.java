package com.hackathon.backend.controllers.hotel.hotelFeatures;

import com.hackathon.backend.services.hotel.hotelFeatures.RoomFeaturesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoomFeaturesController {

    private final RoomFeaturesService roomFeaturesService;

    @Autowired
    private RoomFeaturesController(RoomFeaturesService roomFeaturesService){
        this.roomFeaturesService = roomFeaturesService;
    }

    @PostMapping(path = "${CREATE_ROOM_FEATURE_PATH}")
    public ResponseEntity<String> createRoomFeature(@RequestParam("roomFeature") String roomFeature){
        return roomFeaturesService.createRoomFeature(roomFeature);
    }

    @GetMapping(path = "${GET_ROOM_FEATURES_PATH}")
    public ResponseEntity<?> getRoomFeatures(){
        return roomFeaturesService.getRoomFeatures();
    }

    @PutMapping(path = "${EDIT_ROOM_FEATURE_PATH}")
    public ResponseEntity<String> editRoomFeature(@PathVariable("featureId") int featureId,
                                             String roomFeature){
        return roomFeaturesService.editRoomFeature(featureId,roomFeature);
    }

    @DeleteMapping(path = "${DELETE_ROOM_FEATURE_PATH}")
    public ResponseEntity<String> deleteRoomFeature(@PathVariable("featureId") int featureId){
        return roomFeaturesService.deleteRoomFeature(featureId);
    }
}
