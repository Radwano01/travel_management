package com.hackathon.backend.controllers.hotel.hotelFeatures;

import com.hackathon.backend.services.hotel.hotelFeatures.HotelFeaturesRelationsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class HotelFeaturesRelationsController {

    private final HotelFeaturesRelationsService hotelFeaturesRelationsService;

    @Autowired
    public HotelFeaturesRelationsController(HotelFeaturesRelationsService hotelFeaturesRelationsService) {
        this.hotelFeaturesRelationsService = hotelFeaturesRelationsService;
    }

    @PostMapping(path = "${HOTEL_FEATURE_RELATIONS_PATH}")
    public ResponseEntity<String> addHotelFeatureToHotel(@PathVariable("hotelId") long hotelId,
                                                         @PathVariable("featureId") int featureId){
        try {
            return hotelFeaturesRelationsService.addHotelFeatureToHotel(hotelId, featureId);
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
            return hotelFeaturesRelationsService.removeHotelFeatureFromHotel(hotelId, featureId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
