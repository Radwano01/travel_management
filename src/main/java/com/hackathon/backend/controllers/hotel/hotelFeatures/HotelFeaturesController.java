package com.hackathon.backend.controllers.hotel.hotelFeatures;

import com.hackathon.backend.dto.hotelDto.features.HotelFeatureDto;
import com.hackathon.backend.services.hotel.hotelFeatures.HotelFeaturesService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class HotelFeaturesController {

    private final HotelFeaturesService hotelFeaturesService;

    @Autowired
    private HotelFeaturesController(HotelFeaturesService hotelFeaturesService){
        this.hotelFeaturesService = hotelFeaturesService;
    }

    @PostMapping(path = "${CREATE_HOTEL_FEATURE_PATH}")
    public ResponseEntity<String> createHotelFeature(@RequestBody HotelFeatureDto hotelFeatureDto){
        try {
            return hotelFeaturesService.createHotelFeature(hotelFeatureDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_HOTEL_FEATURES_PATH}")
    public ResponseEntity<?> getRoomFeatures(){
        try {
            return hotelFeaturesService.getHotelFeatures();
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_HOTEL_FEATURE_PATH}")
    public ResponseEntity<String> editHotelFeature(@PathVariable("featureId") int featureId,
                                                   @RequestBody HotelFeatureDto hotelFeatureDto){
        try {
            return hotelFeaturesService.editHotelFeature(featureId, hotelFeatureDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_HOTEL_FEATURE_PATH}")
    public ResponseEntity<String> deleteHotelFeature(@PathVariable("featureId") int featureId){
        try {
            return hotelFeaturesService.deleteHotelFeature(featureId);
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}
