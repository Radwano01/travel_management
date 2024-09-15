package com.hackathon.backend.services.hotel.hotelFeatures.impl;

import com.hackathon.backend.dto.hotelDto.features.GetHotelFeaturesDto;
import com.hackathon.backend.dto.hotelDto.features.HotelFeatureDto;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
import com.hackathon.backend.repositories.hotel.hotelFeatures.HotelFeaturesRepository;
import com.hackathon.backend.services.hotel.hotelFeatures.HotelFeaturesService;
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
public class HotelFeaturesServiceImpl implements HotelFeaturesService {

    private final HotelFeaturesRepository hotelFeaturesRepository;
    private final RoomDetailsRepository roomDetailsRepository;

    @Autowired
    public HotelFeaturesServiceImpl(HotelFeaturesRepository hotelFeaturesRepository,
                                    RoomDetailsRepository roomDetailsRepository){
        this.hotelFeaturesRepository = hotelFeaturesRepository;
        this.roomDetailsRepository = roomDetailsRepository;
    }

    //hotel features
    //note: create feature without connecting it with hotels details
    // because there is many to many with join table

    @Override
    public ResponseEntity<String> createHotelFeature(HotelFeatureDto hotelFeatureDto) {
        String feature = hotelFeatureDto.getHotelFeature().trim();

        ResponseEntity<String> checkResult = checkIfFeatureAlreadyExist(feature);
        if(!checkResult.getStatusCode().equals(HttpStatus.OK)){
            return checkResult;
        }

        hotelFeaturesRepository.save(new HotelFeaturesEntity(feature));

        return ResponseEntity.ok("Hotel feature created successfully " + feature);
    }

    @Override
    public ResponseEntity<List<GetHotelFeaturesDto>> getHotelFeatures() {
        return ResponseEntity.ok(hotelFeaturesRepository.findAllHotelFeatures());
    }

    @Transactional
    @Override
    public ResponseEntity<String> editHotelFeature(int featureId, HotelFeatureDto hotelFeatureDto){
        if(!checkIfSentEmptyData(hotelFeatureDto)){
            return badRequestException("you sent an empty data to change");
        }

        String hotelFeature = hotelFeatureDto.getHotelFeature().trim();

        HotelFeaturesEntity hotelFeatures = findHotelFeatureById(featureId);

        hotelFeatures.setHotelFeatures(hotelFeature);

        hotelFeaturesRepository.save(hotelFeatures);

        return ResponseEntity.ok("Hotel Feature edited successfully " + hotelFeature);
    }


    private ResponseEntity<String> checkIfFeatureAlreadyExist(String feature){
        boolean existsHotelFeature = hotelFeaturesRepository.existsHotelFeatureByHotelFeatures(feature);

        if(existsHotelFeature){
            return alreadyValidException("Hotel Feature already exists");
        }
        return ResponseEntity.ok("OK");
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteHotelFeature(int featureId) {
        HotelFeaturesEntity hotelFeatures = findHotelFeatureById(featureId);

        for (RoomDetailsEntity roomDetails : hotelFeatures.getRoomDetails()) {
            roomDetails.getHotelFeatures().remove(hotelFeatures);
            roomDetailsRepository.save(roomDetails);
        }

        hotelFeaturesRepository.delete(hotelFeatures);

        return ResponseEntity.ok("Hotel feature deleted successfully");
    }

    private HotelFeaturesEntity findHotelFeatureById(int featureId){
        return hotelFeaturesRepository.findById(featureId)
                .orElseThrow(() -> new EntityNotFoundException("No such feature has this id"));
    }
}
