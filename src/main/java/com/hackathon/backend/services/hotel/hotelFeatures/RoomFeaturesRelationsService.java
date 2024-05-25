package com.hackathon.backend.services.hotel.hotelFeatures;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.features.RoomFeaturesUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class RoomFeaturesRelationsService {

    private final HotelUtils hotelUtils;
    private final RoomFeaturesUtils roomFeaturesUtils;

    @Autowired
    public RoomFeaturesRelationsService(HotelUtils hotelUtils,
                                        RoomFeaturesUtils roomFeaturesUtils) {
        this.hotelUtils = hotelUtils;
        this.roomFeaturesUtils = roomFeaturesUtils;
    }

    @Transactional
    public ResponseEntity<?> addRoomFeatureToHotel(long hotelId,
                                                   int featureId) {
        try{
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            RoomFeaturesEntity roomFeatures = roomFeaturesUtils.findById(featureId);
            Optional<RoomFeaturesEntity> roomFeaturesEntityOptional = hotel.getRoomDetails()
                    .getRoomFeatures().stream().filter((feature)-> feature == roomFeatures).findFirst();
            if(roomFeaturesEntityOptional.isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("This Room feature is already valid for this hotel");
            }
            hotel.getRoomDetails().getRoomFeatures().add(roomFeatures);
            hotelUtils.save(hotel);
            roomFeaturesUtils.save(roomFeatures);
            return ResponseEntity.ok("Room feature added successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> removeRoomFeatureFromHotel(long hotelId, int featureId) {
        try{
            HotelEntity hotelEntity = hotelUtils.findHotelById(hotelId);
            RoomFeaturesEntity roomFeaturesEntity = roomFeaturesUtils.findById(featureId);
            if(hotelEntity != null && roomFeaturesEntity != null) {
                Optional<RoomFeaturesEntity> roomFeaturesEntityOptional = hotelEntity.getRoomDetails().getRoomFeatures().stream()
                        .filter((feature) -> feature.getId() == featureId)
                        .findFirst();
                if(roomFeaturesEntityOptional.isPresent()) {
                    hotelEntity.getRoomDetails().getRoomFeatures().remove(roomFeaturesEntityOptional.get());
                    hotelUtils.save(hotelEntity);
                    roomFeaturesUtils.save(roomFeaturesEntity);
                    return ResponseEntity.ok("Room feature removed from this hotel");
                }else{
                    return notFoundException("Room feature not found in this hotel");
                }
            }else{
                return notFoundException("Hotel or room feature not found");
            }
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

}
