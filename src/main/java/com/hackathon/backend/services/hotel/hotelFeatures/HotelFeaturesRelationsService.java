package com.hackathon.backend.services.hotel.hotelFeatures;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
import com.hackathon.backend.repositories.hotel.hotelFeatures.HotelFeaturesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class HotelFeaturesRelationsService {

    private final HotelFeaturesRepository hotelFeaturesRepository;
    private final RoomDetailsRepository roomDetailsRepository;
    private final HotelRepository hotelRepository;


    @Autowired
    public HotelFeaturesRelationsService(HotelFeaturesRepository hotelFeaturesRepository,
                                         RoomDetailsRepository roomDetailsRepository,
                                         HotelRepository hotelRepository) {
        this.hotelFeaturesRepository = hotelFeaturesRepository;
        this.roomDetailsRepository = roomDetailsRepository;
        this.hotelRepository = hotelRepository;
    }

    @Transactional
    public ResponseEntity<String> addHotelFeatureToHotel(long hotelId, int featureId) {
        HotelEntity hotel = findHotelById(hotelId);

        RoomDetailsEntity roomDetails = hotel.getRoomDetails();

        if(checkIfFeatureAlreadyExist(roomDetails, featureId) != null){
            return alreadyValidException("This Feature is already existed");
        }

        HotelFeaturesEntity hotelFeatures = findHotelFeatureById(featureId);

        roomDetails.getHotelFeatures().add(hotelFeatures);

        roomDetailsRepository.save(roomDetails);

        return ResponseEntity.ok("Hotel feature added successfully " + hotelFeatures.getHotelFeatures());
    }

    private HotelEntity findHotelById(long hotelId){
        return hotelRepository.findById(hotelId)
                .orElseThrow(()-> new EntityNotFoundException("Hotel id not found"));
    }

    @Transactional
    public ResponseEntity<String> removeHotelFeatureFromHotel(long hotelId, int featureId) {
        HotelEntity hotelEntity = findHotelById(hotelId);

        RoomDetailsEntity roomDetails = hotelEntity.getRoomDetails();

        HotelFeaturesEntity hotelFeatures = checkIfFeatureAlreadyExist(roomDetails, featureId);

        if(hotelFeatures == null){
            return notFoundException("This feature is not found");
        }

        roomDetails.getHotelFeatures().remove(hotelFeatures);

        roomDetailsRepository.save(roomDetails);

        return ResponseEntity.ok("Hotel feature removed successfully");
    }

    private HotelFeaturesEntity findHotelFeatureById(int featureId){
        return hotelFeaturesRepository.findById(featureId)
                .orElseThrow(()-> new EntityNotFoundException("No such feature has this id"));
    }

    private HotelFeaturesEntity checkIfFeatureAlreadyExist(RoomDetailsEntity roomDetails, int featureId){
        Optional<HotelFeaturesEntity> exists = roomDetails.getHotelFeatures().stream()
                .filter(feature -> feature.getId() == featureId)
                .findFirst();

        return exists.orElse(null);
    }
}
