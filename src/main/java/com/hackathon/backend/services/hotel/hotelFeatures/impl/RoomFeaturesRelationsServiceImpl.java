package com.hackathon.backend.services.hotel.hotelFeatures.impl;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
import com.hackathon.backend.repositories.hotel.hotelFeatures.RoomFeaturesRepository;
import com.hackathon.backend.services.hotel.hotelFeatures.RoomFeaturesRelationsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class RoomFeaturesRelationsServiceImpl implements RoomFeaturesRelationsService {

    private final RoomFeaturesRepository roomFeaturesRepository;
    private final RoomDetailsRepository roomDetailsRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public RoomFeaturesRelationsServiceImpl(RoomFeaturesRepository roomFeaturesRepository,
                                            RoomDetailsRepository roomDetailsRepository,
                                            HotelRepository hotelRepository) {
        this.roomFeaturesRepository = roomFeaturesRepository;
        this.roomDetailsRepository = roomDetailsRepository;
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    @Override
    public ResponseEntity<String> addRoomFeatureToHotel(long hotelId, int featureId) {
        HotelEntity hotel = findHotelById(hotelId);

        RoomDetailsEntity roomDetails = hotel.getRoomDetails();

        if(checkIfFeatureAlreadyExist(roomDetails, featureId) != null){
            return alreadyValidException("This Room feature is already valid for this hotel");
        }

        RoomFeaturesEntity roomFeatures = findRoomFeatureById(featureId);

        hotel.getRoomDetails().getRoomFeatures().add(roomFeatures);

        roomDetailsRepository.save(roomDetails);

        return ResponseEntity.ok("Room feature added successfully " + roomFeatures.getRoomFeatures());
    }

    private HotelEntity findHotelById(long hotelId){
        return hotelRepository.findById(hotelId)
                .orElseThrow(()-> new EntityNotFoundException("Hotel id not found"));
    }

    @Transactional
    @Override
    public ResponseEntity<String> removeRoomFeatureFromHotel(long hotelId, int featureId) {
        HotelEntity hotelEntity = findHotelById(hotelId);

        RoomDetailsEntity roomDetails = hotelEntity.getRoomDetails();

        RoomFeaturesEntity roomFeatures = checkIfFeatureAlreadyExist(roomDetails, featureId);

        if(roomFeatures == null){
            return notFoundException("This feature is not found");
        }

        hotelEntity.getRoomDetails().getRoomFeatures().remove(roomFeatures);

        roomDetailsRepository.save(roomDetails);

        return ResponseEntity.ok("Room feature removed from this hotel");
    }

    private RoomFeaturesEntity findRoomFeatureById(int featureId){
        return roomFeaturesRepository.findById(featureId)
                .orElseThrow(()-> new EntityNotFoundException("No such feature has this id"));
    }

    private RoomFeaturesEntity checkIfFeatureAlreadyExist(RoomDetailsEntity roomDetails, int featureId){
        Optional<RoomFeaturesEntity> exists = roomDetails.getRoomFeatures().stream()
                .filter(feature -> feature.getId() == featureId)
                .findFirst();

        return exists.orElse(null);
    }
}
