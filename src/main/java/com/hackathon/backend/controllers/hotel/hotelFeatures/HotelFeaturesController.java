package com.hackathon.backend.controllers.hotel.hotelFeatures;

import com.hackathon.backend.dto.hotelDto.features.HotelFeatureDto;
import com.hackathon.backend.services.hotel.hotelFeatures.impl.HotelFeaturesServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class HotelFeaturesController {

    private final HotelFeaturesServiceImpl hotelFeaturesServiceImpl;

    @Autowired
    private HotelFeaturesController(HotelFeaturesServiceImpl hotelFeaturesServiceImpl){
        this.hotelFeaturesServiceImpl = hotelFeaturesServiceImpl;
    }

    @PostMapping(path = "${CREATE_HOTEL_FEATURE_PATH}")
    public ResponseEntity<String> createHotelFeature(@RequestBody HotelFeatureDto hotelFeatureDto){
        try {
            return hotelFeaturesServiceImpl.createHotelFeature(hotelFeatureDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_HOTEL_FEATURES_PATH}")
    public ResponseEntity<?> getRoomFeatures(){
        try {
            return hotelFeaturesServiceImpl.getHotelFeatures();
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_HOTEL_FEATURE_PATH}")
    public ResponseEntity<String> editHotelFeature(@PathVariable("featureId") int featureId,
                                                   @RequestBody HotelFeatureDto hotelFeatureDto){
        try {
            return hotelFeaturesServiceImpl.editHotelFeature(featureId, hotelFeatureDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_HOTEL_FEATURE_PATH}")
    public ResponseEntity<String> deleteHotelFeature(@PathVariable("featureId") int featureId){
        try {
            return hotelFeaturesServiceImpl.deleteHotelFeature(featureId);
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}
