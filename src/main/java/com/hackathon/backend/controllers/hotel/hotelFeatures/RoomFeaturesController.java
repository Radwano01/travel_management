package com.hackathon.backend.controllers.hotel.hotelFeatures;

import com.hackathon.backend.dto.hotelDto.features.RoomFeatureDto;
import com.hackathon.backend.services.hotel.hotelFeatures.impl.RoomFeaturesServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoomFeaturesController {

    private final RoomFeaturesServiceImpl roomFeaturesServiceImpl;

    @Autowired
    private RoomFeaturesController(RoomFeaturesServiceImpl roomFeaturesServiceImpl){
        this.roomFeaturesServiceImpl = roomFeaturesServiceImpl;
    }

    @PostMapping(path = "${CREATE_ROOM_FEATURE_PATH}")
    public ResponseEntity<String> createRoomFeature(@RequestBody RoomFeatureDto roomFeatureDto){
        try {
            return roomFeaturesServiceImpl.createRoomFeature(roomFeatureDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_ROOM_FEATURES_PATH}")
    public ResponseEntity<?> getRoomFeatures(){
        try {
            return roomFeaturesServiceImpl.getRoomFeatures();
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_ROOM_FEATURE_PATH}")
    public ResponseEntity<String> editRoomFeature(@PathVariable("featureId") int featureId,
                                                  @RequestBody RoomFeatureDto roomFeatureDto){
        try {
            return roomFeaturesServiceImpl.editRoomFeature(featureId, roomFeatureDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_ROOM_FEATURE_PATH}")
    public ResponseEntity<String> deleteRoomFeature(@PathVariable("featureId") int featureId){
        try {
            return roomFeaturesServiceImpl.deleteRoomFeature(featureId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
