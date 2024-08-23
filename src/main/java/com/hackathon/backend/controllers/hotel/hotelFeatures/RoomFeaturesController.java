package com.hackathon.backend.controllers.hotel.hotelFeatures;

import com.hackathon.backend.dto.hotelDto.features.RoomFeatureDto;
import com.hackathon.backend.services.hotel.hotelFeatures.RoomFeaturesService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoomFeaturesController {

    private final RoomFeaturesService roomFeaturesService;

    @Autowired
    private RoomFeaturesController(RoomFeaturesService roomFeaturesService){
        this.roomFeaturesService = roomFeaturesService;
    }

    @PostMapping(path = "${CREATE_ROOM_FEATURE_PATH}")
    public ResponseEntity<String> createRoomFeature(@RequestBody RoomFeatureDto roomFeatureDto){
        try {
            return roomFeaturesService.createRoomFeature(roomFeatureDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_ROOM_FEATURES_PATH}")
    public ResponseEntity<?> getRoomFeatures(){
        try {
            return roomFeaturesService.getRoomFeatures();
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_ROOM_FEATURE_PATH}")
    public ResponseEntity<String> editRoomFeature(@PathVariable("featureId") int featureId,
                                                  @RequestBody RoomFeatureDto roomFeatureDto){
        try {
            return roomFeaturesService.editRoomFeature(featureId, roomFeatureDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_ROOM_FEATURE_PATH}")
    public ResponseEntity<String> deleteRoomFeature(@PathVariable("featureId") int featureId){
        try {
            return roomFeaturesService.deleteRoomFeature(featureId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
