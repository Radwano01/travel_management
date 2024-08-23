package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDetailsDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceDetailsDto;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.utilities.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;


@Service
public class PlaceDetailsService {
    private final PlaceRepository placeRepository;
    private final S3Service s3Service;

    @Autowired
    public PlaceDetailsService(PlaceRepository placeRepository,
                               S3Service s3Service) {
        this.placeRepository = placeRepository;
        this.s3Service = s3Service;
    }

    public ResponseEntity<GetPlaceDetailsDto> getSinglePlaceDetails(int placeId) {
        return ResponseEntity.ok(placeRepository.findPlaceWithPlaceDetailsByPlaceId(placeId));
    }

    @Transactional
    public ResponseEntity<String> editPlaceDetails(int placeId, EditPlaceDetailsDto editPlaceDetailsDto) {
        if(!checkIfSentEmptyData(editPlaceDetailsDto)){
            return badRequestException("you sent an empty data to change");
        }

        PlaceEntity place = getPlaceById(placeId);

        updateToNewData(place.getPlaceDetails(), editPlaceDetailsDto);

        placeRepository.save(place);

        return ResponseEntity.ok
        (
            new GetPlaceDetailsDto(
                place.getId(),
                place.getPlace(),
                place.getMainImage(),
                place.getPlaceDetails().getImageOne(),
                place.getPlaceDetails().getImageTwo(),
                place.getPlaceDetails().getImageThree(),
                place.getPlaceDetails().getDescription()
        ).toString());
    }

    private PlaceEntity getPlaceById(int placeId){
        return placeRepository.findById(placeId)
                .orElseThrow(()-> new EntityNotFoundException("Not such place has this id"));
    }

    private void updateToNewData(PlaceDetailsEntity placeDetails, EditPlaceDetailsDto editPlaceDetailsDto){
        if(editPlaceDetailsDto.getImageOne() != null){
            s3Service.deleteFile(placeDetails.getImageOne());
            String placeDetailsImageOneName = s3Service.uploadFile(editPlaceDetailsDto.getImageOne());
            placeDetails.setImageOne(placeDetailsImageOneName);
        }
        if(editPlaceDetailsDto.getImageTwo() != null){
            s3Service.deleteFile(placeDetails.getImageTwo());
            String placeDetailsImageTwoName = s3Service.uploadFile(editPlaceDetailsDto.getImageTwo());
            placeDetails.setImageTwo(placeDetailsImageTwoName);
        }
        if(editPlaceDetailsDto.getImageThree() != null){
            s3Service.deleteFile(placeDetails.getImageThree());
            String placeDetailsImageThreeName = s3Service.uploadFile(editPlaceDetailsDto.getImageThree());
            placeDetails.setImageThree(placeDetailsImageThreeName);
        }
        if(editPlaceDetailsDto.getDescription() != null){
            placeDetails.setDescription(editPlaceDetailsDto.getDescription());
        }
    }
}
