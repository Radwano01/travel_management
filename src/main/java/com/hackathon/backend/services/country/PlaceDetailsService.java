package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDetailsDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceDetailsDto;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.PlaceDetailsUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.hackathon.backend.utilities.ErrorUtils.*;

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
            GetPlaceDetailsDto getPlaceDetailsDto = new GetPlaceDetailsDto(
                    place.getId(),
                    place.getPlace(),
                    place.getMainImage(),
                    place.getPlaceDetails().getImageOne(),
                    place.getPlaceDetails().getImageTwo(),
                    place.getPlaceDetails().getImageThree(),
                    place.getPlaceDetails().getDescription()
            );
            return ResponseEntity.ok(getPlaceDetailsDto);
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> editPlaceDetails(int placeId,
                                                EditPlaceDetailsDto editPlaceDetailsDto) {
        try{
            if(!placeDetailsUtils.checkHelper(editPlaceDetailsDto)){
                return badRequestException("you sent an empty data to change");
            }
            PlaceEntity place = placeUtils.findById(placeId);
            placeDetailsUtils.editHelper(place.getPlaceDetails(), editPlaceDetailsDto);
            placeDetailsUtils.save(place.getPlaceDetails());
            placeUtils.save(place);
            return ResponseEntity.ok("Place details edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
