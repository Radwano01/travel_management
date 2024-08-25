package com.hackathon.backend.services.hotel.hotelFeatures.impl;


import com.hackathon.backend.dto.hotelDto.features.RoomFeatureDto;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.repositories.hotel.hotelFeatures.RoomFeaturesRepository;
import com.hackathon.backend.services.hotel.hotelFeatures.RoomFeaturesService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class RoomFeaturesServiceImpl implements RoomFeaturesService {
    private final RoomFeaturesRepository roomFeaturesRepository;

    @Autowired
    public RoomFeaturesServiceImpl(RoomFeaturesRepository roomFeaturesRepository){
        this.roomFeaturesRepository = roomFeaturesRepository;
    }

    //room features
    //note: create feature without connecting it with rooms details
    // because there is many to many with join table

    @Override
    public ResponseEntity<String> createRoomFeature(RoomFeatureDto roomFeatureDto) {
        String roomFeature = roomFeatureDto.getRoomFeature().trim();

        ResponseEntity<String> checkResult = checkIfFeatureAlreadyExist(roomFeature);
        if(!checkResult.getStatusCode().equals(HttpStatus.OK)){
            return checkResult;
        }

        roomFeaturesRepository.save(new RoomFeaturesEntity(roomFeature));

        return ResponseEntity.ok("Room feature created successfully " + roomFeature);
    }

    @Override
    public ResponseEntity<List<RoomFeaturesEntity>> getRoomFeatures() {
        return ResponseEntity.ok(roomFeaturesRepository.findAll());
    }

    @Transactional
    @Override
    public ResponseEntity<String> editRoomFeature(int featureId, RoomFeatureDto roomFeatureDto){
        if(!checkIfSentEmptyData(roomFeatureDto)){
            return badRequestException("you sent an empty data to change");
        }

        String roomFeature = roomFeatureDto.getRoomFeature().trim();

        RoomFeaturesEntity roomFeatures = findRoomFeatureById(featureId);

        roomFeatures.setRoomFeatures(roomFeature);

        roomFeaturesRepository.save(roomFeatures);

        return ResponseEntity.ok("Room Feature edited successfully " + roomFeature);
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteRoomFeature(int featureId) {
        RoomFeaturesEntity roomFeatures = findRoomFeatureById(featureId);

        for (RoomDetailsEntity roomDetails : roomFeatures.getRoomDetails()) {
            roomDetails.getRoomFeatures().remove(roomFeatures);
        }

        roomFeaturesRepository.delete(roomFeatures);

        return ResponseEntity.ok("Room feature deleted successfully");
    }

    private RoomFeaturesEntity findRoomFeatureById(int featureId){
        return roomFeaturesRepository.findById(featureId)
                .orElseThrow(() -> new EntityNotFoundException("No such feature has this id"));
    }

    private ResponseEntity<String> checkIfFeatureAlreadyExist(String feature){
        boolean existsHotelFeature = roomFeaturesRepository.existsRoomFeatureByRoomFeatures(feature);

        if(existsHotelFeature){
            return alreadyValidException("Hotel Feature already exists");
        }
        return ResponseEntity.ok("OK");
    }
}
