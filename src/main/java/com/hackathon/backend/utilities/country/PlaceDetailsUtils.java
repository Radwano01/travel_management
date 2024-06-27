package com.hackathon.backend.utilities.country;

import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDetailsDto;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.repositories.country.PlaceDetailsRepository;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlaceDetailsUtils {

    private final PlaceDetailsRepository placeDetailsRepository;

    private final S3Service s3Service;

    @Autowired
    public PlaceDetailsUtils(PlaceDetailsRepository placeDetailsRepository,
                             S3Service s3Service) {
        this.placeDetailsRepository = placeDetailsRepository;
        this.s3Service = s3Service;
    }

    public void save(PlaceDetailsEntity placeDetails) {
        placeDetailsRepository.save(placeDetails);
    }

    public PlaceDetailsEntity findById(int placeDetailsId) {
        return placeDetailsRepository.findById(placeDetailsId)
                .orElseThrow(()-> new EntityNotFoundException("Place details id not found"));
    }

    public void delete(PlaceDetailsEntity placeDetails) {
        placeDetailsRepository.delete(placeDetails);
    }

    public boolean checkHelper(EditPlaceDetailsDto editPlaceDetailsDto){
        return  editPlaceDetailsDto.getImageOne() != null ||
                editPlaceDetailsDto.getImageTwo() != null ||
                editPlaceDetailsDto.getImageThree() != null ||
                editPlaceDetailsDto.getDescription() != null;
    }

    public void editHelper(PlaceDetailsEntity placeDetails,
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
