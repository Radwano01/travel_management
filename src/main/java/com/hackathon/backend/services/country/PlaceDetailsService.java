package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.placeDto.PlaceDetailsDto;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.utilities.country.PlaceDetailsUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PlaceDetailsService {
    private final PlaceUtils placeUtils;
    private final PlaceDetailsUtils placeDetailsUtils;

    @Autowired
    public PlaceDetailsService(PlaceUtils placeUtils,
                               PlaceDetailsUtils placeDetailsUtils) {
        this.placeUtils = placeUtils;
        this.placeDetailsUtils = placeDetailsUtils;
    }

    public ResponseEntity<?> getSinglePlaceDetails(int placeId) {
        try {
            PlaceEntity place = placeUtils.findById(placeId);
            PlaceDetailsDto placeDetailsDto = new PlaceDetailsDto(
                    place.getId(),
                    place.getPlace(),
                    place.getMainImage(),
                    place.getPlaceDetails().getImageOne(),
                    place.getPlaceDetails().getImageTwo(),
                    place.getPlaceDetails().getImageThree(),
                    place.getPlaceDetails().getDescription()
            );
            return ResponseEntity.ok(placeDetailsDto);
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editPlaceDetails(int placeDetailsId,
                                              PlaceDetailsDto placeDetailsDto) {
        try{
            PlaceDetailsEntity placeDetails = placeDetailsUtils.findById(placeDetailsId);
            editHelper(placeDetails,placeDetailsDto);
            placeDetailsUtils.save(placeDetails);
            return ResponseEntity.ok("Place details edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(PlaceDetailsEntity placeDetails,
                            PlaceDetailsDto placeDetailsDto){
        if(placeDetailsDto.getImageOne() != null){
            placeDetails.setImageOne(placeDetailsDto.getImageOne());
        }
        if(placeDetailsDto.getImageTwo() != null){
            placeDetails.setImageTwo(placeDetailsDto.getImageTwo());
        }
        if(placeDetailsDto.getImageThree() != null){
            placeDetails.setImageThree(placeDetailsDto.getImageThree());
        }
        if(placeDetailsDto.getDescription() != null){
            placeDetails.setDescription(placeDetailsDto.getDescription());
        }
    }
}
