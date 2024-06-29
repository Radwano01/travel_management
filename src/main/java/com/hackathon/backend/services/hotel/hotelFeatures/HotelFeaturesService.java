package com.hackathon.backend.services.hotel.hotelFeatures;

import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.features.HotelFeaturesUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class HotelFeaturesService {

    private final RoomDetailsUtils roomDetailsUtils;
    private final HotelFeaturesUtils hotelFeaturesUtils;

    @Autowired
    public HotelFeaturesService(RoomDetailsUtils roomDetailsUtils,
                                HotelFeaturesUtils hotelFeaturesUtils){
        this.roomDetailsUtils = roomDetailsUtils;
        this.hotelFeaturesUtils = hotelFeaturesUtils;
    }

    //hotel features
    //note: create feature without connecting it with hotels details
    // because there is many to many with join table

    public ResponseEntity<String> createHotelFeature(String hotelFeature) {
        try{
            String feature = hotelFeature.trim();
            boolean existsHotelFeature = hotelFeaturesUtils.existsHotelFeatureByHotelFeatures(feature);
            if(existsHotelFeature){
                return alreadyValidException("Hotel Feature already exists");
            }
            HotelFeaturesEntity hotelFeatures = new HotelFeaturesEntity(
                    feature
            );
            hotelFeaturesUtils.save(hotelFeatures);
            return ResponseEntity.ok("Hotel feature created successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getHotelFeatures() {
        try{
            List<HotelFeaturesEntity> hotelFeatures = hotelFeaturesUtils.findAll();
            return ResponseEntity.ok(hotelFeatures);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> editHotelFeature(int featureId,
                                              String hotelFeature){
        try{
            if(hotelFeature == null){
                return badRequestException("you sent an empty data to change");
            }
            HotelFeaturesEntity hotelFeatures = hotelFeaturesUtils.findById(featureId);
            hotelFeatures.setHotelFeatures(hotelFeature);
            hotelFeaturesUtils.save(hotelFeatures);
            return ResponseEntity.ok("Hotel Feature edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteHotelFeature(int featureId) {
        try {
            HotelFeaturesEntity hotelFeatures = hotelFeaturesUtils.findById(featureId);
            for (RoomDetailsEntity roomDetails : hotelFeatures.getRoomDetails()) {
                roomDetails.getHotelFeatures().remove(hotelFeatures);
                roomDetailsUtils.save(roomDetails);
            }
            hotelFeaturesUtils.delete(hotelFeatures);
            return ResponseEntity.ok("Hotel feature deleted successfully");

        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}
