package com.hackathon.backend.services.hotel.featuresSerivces;


import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.features.RoomFeaturesUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class RoomFeaturesService {
    private final RoomDetailsUtils roomDetailsUtils;
    private final RoomFeaturesUtils roomFeaturesUtils;

    @Autowired
    public RoomFeaturesService(RoomDetailsUtils roomDetailsUtils,
                               RoomFeaturesUtils roomFeaturesUtils){
        this.roomDetailsUtils = roomDetailsUtils;
        this.roomFeaturesUtils = roomFeaturesUtils;
    }

    //room features
    //note: create feature without connecting it with rooms details
    // because there is many to many with join table


    public ResponseEntity<?> createRoomFeature(String roomFeature) {
        try{
            String feature = roomFeature.trim();
            boolean existsRoomFeature = roomFeaturesUtils.existsRoomFeatureByRoomFeatures(feature);
            if(existsRoomFeature){
                return new ResponseEntity<>("Room Feature already exists", HttpStatus.CONFLICT);
            }
            RoomFeaturesEntity roomFeatures = new RoomFeaturesEntity(
                    roomFeature
            );
            roomFeaturesUtils.save(roomFeatures);
            return ResponseEntity.ok("Room feature created successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getRoomFeatures() {
        try{
            List<RoomFeaturesEntity> roomFeatures = roomFeaturesUtils.findAll();
            return ResponseEntity.ok(roomFeatures);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editRoomFeature(int featureId,
                                             String roomFeature){
        try{
            RoomFeaturesEntity roomFeatures = roomFeaturesUtils.findById(featureId);
            if(roomFeature != null){
                roomFeatures.setRoomFeatures(roomFeature);
            }
            roomFeaturesUtils.save(roomFeatures);
            return ResponseEntity.ok("Room Feature edited successfully");
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteRoomFeature(int featureId) {
        try {
            RoomFeaturesEntity roomFeatures = roomFeaturesUtils.findById(featureId);
            if (roomFeatures != null) {
                List<RoomDetailsEntity> roomDetailsEntities = roomDetailsUtils.findAll();
                for (RoomDetailsEntity roomDetails : roomDetailsEntities) {
                    if (roomDetails.getRoomFeatures() != null) {
                        List<RoomFeaturesEntity> roomFeaturesEntities = roomDetails.getRoomFeatures();
                        boolean removeIf = roomFeaturesEntities.removeIf((rf) -> rf.getId() == featureId);
                        if (removeIf) {
                            roomDetailsUtils.save(roomDetails);
                        }
                    } else {
                        return notFoundException("This room has no features");
                    }
                }
                roomFeaturesUtils.deleteById(featureId);
                return ResponseEntity.ok("Room feature deleted successfully");
            } else {
                return notFoundException("Room feature not found");
            }
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

}
