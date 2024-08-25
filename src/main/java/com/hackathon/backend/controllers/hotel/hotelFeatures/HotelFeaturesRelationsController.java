package com.hackathon.backend.controllers.hotel.hotelFeatures;

import com.hackathon.backend.services.hotel.hotelFeatures.impl.HotelFeaturesRelationsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class HotelFeaturesRelationsController {

    private final HotelFeaturesRelationsServiceImpl hotelFeaturesRelationsServiceImpl;

    @Autowired
    public HotelFeaturesRelationsController(HotelFeaturesRelationsServiceImpl hotelFeaturesRelationsServiceImpl) {
        this.hotelFeaturesRelationsServiceImpl = hotelFeaturesRelationsServiceImpl;
    }

    @PostMapping(path = "${HOTEL_FEATURE_RELATIONS_PATH}")
    public ResponseEntity<String> addHotelFeatureToHotel(@PathVariable("hotelId") long hotelId,
                                                         @PathVariable("featureId") int featureId){
        try {
            return hotelFeaturesRelationsServiceImpl.addHotelFeatureToHotel(hotelId, featureId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${HOTEL_FEATURE_RELATIONS_PATH}")
    public ResponseEntity<String> removeHotelFeatureToHotel(@PathVariable("hotelId") long hotelId,
                                                            @PathVariable("featureId") int featureId){
        try {
            return hotelFeaturesRelationsServiceImpl.removeHotelFeatureFromHotel(hotelId, featureId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
