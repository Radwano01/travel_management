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

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PlaceDetailsService {
    private final PlaceUtils placeUtils;
    private final PlaceDetailsUtils placeDetailsUtils;
    private final S3Service s3Service;

    @Autowired
    public PlaceDetailsService(PlaceUtils placeUtils,
                               PlaceDetailsUtils placeDetailsUtils,
                               S3Service s3Service) {
        this.placeUtils = placeUtils;
        this.placeDetailsUtils = placeDetailsUtils;
        this.s3Service = s3Service;
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
    public ResponseEntity<?> editPlaceDetails(int placeId,
                                              EditPlaceDetailsDto editPlaceDetailsDto) {
        try{
            PlaceEntity place = placeUtils.findById(placeId);
            editHelper(place.getPlaceDetails(), editPlaceDetailsDto);
            placeDetailsUtils.save(place.getPlaceDetails());
            placeUtils.save(place);
            return ResponseEntity.ok("Place details edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(PlaceDetailsEntity placeDetails,
                            EditPlaceDetailsDto editPlaceDetailsDto){
        if(editPlaceDetailsDto.getImageOne() != null){
            String placeDetailsImageOneName = s3Service.uploadFile(editPlaceDetailsDto.getImageOne());
            placeDetails.setImageOne(placeDetailsImageOneName);
        }
        if(editPlaceDetailsDto.getImageTwo() != null){
            String placeDetailsImageTwoName = s3Service.uploadFile(editPlaceDetailsDto.getImageTwo());
            placeDetails.setImageTwo(placeDetailsImageTwoName);
        }
        if(editPlaceDetailsDto.getImageThree() != null){
            String placeDetailsImageThreeName = s3Service.uploadFile(editPlaceDetailsDto.getImageThree());
            placeDetails.setImageThree(placeDetailsImageThreeName);
        }
        if(editPlaceDetailsDto.getDescription() != null){
            placeDetails.setDescription(editPlaceDetailsDto.getDescription());
        }
    }
}
