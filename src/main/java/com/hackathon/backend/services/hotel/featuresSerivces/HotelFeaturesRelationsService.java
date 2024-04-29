package com.hackathon.backend.services.hotel.featuresSerivces;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.features.HotelFeaturesUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class HotelFeaturesRelationsService {

    private final HotelFeaturesUtils hotelFeaturesUtils;
    private final HotelUtils hotelUtils;


    @Autowired
    public HotelFeaturesRelationsService(HotelFeaturesUtils hotelFeaturesUtils,
                                         HotelUtils hotelUtils) {
        this.hotelFeaturesUtils = hotelFeaturesUtils;
        this.hotelUtils = hotelUtils;
    }

    @Transactional
    public ResponseEntity<?> addHotelFeatureToHotel(long hotelId,
                                                    int featureId) {
        try{
            HotelEntity hotel = hotelUtils.findHotelById(hotelId);
            HotelFeaturesEntity hotelFeatures = hotelFeaturesUtils.findById(featureId);
            Optional<HotelFeaturesEntity> exists = hotel.getRoomDetails().getHotelFeatures().stream()
                    .filter((feature)-> feature.getId() == featureId)
                    .findFirst();
            if(exists.isPresent()){
                return alreadyValidException("This Hotel feature already valid for this hotel");
            }
            hotel.getRoomDetails().getHotelFeatures().add(hotelFeatures);
            hotelUtils.save(hotel);
            hotelFeaturesUtils.save(hotelFeatures);
            return ResponseEntity.ok("Hotel feature added successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> removeHotelFeatureFromHotel(long hotelId, int featureId) {
        try{
            HotelEntity hotelEntity = hotelUtils.findHotelById(hotelId);
            HotelFeaturesEntity hotelFeaturesEntity = hotelFeaturesUtils.findById(featureId);
            if(hotelEntity != null && hotelFeaturesEntity != null) {
                Optional<HotelFeaturesEntity> hotelFeaturesEntityOptional = hotelEntity.getRoomDetails().getHotelFeatures().stream()
                        .filter((feature) -> feature.getId() == featureId)
                        .findFirst();
                if(hotelFeaturesEntityOptional.isPresent()) {
                    hotelEntity.getRoomDetails().getHotelFeatures().remove(hotelFeaturesEntityOptional.get());
                    hotelUtils.save(hotelEntity);
                    hotelFeaturesUtils.save(hotelFeaturesEntity);
                    return ResponseEntity.ok("Hotel feature removed successfully");
                }else{
                    return notFoundException("Hotel feature not found in this hotel");
                }
            }else{
                return notFoundException("Hotel or hotel feature not found");
            }
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

}
