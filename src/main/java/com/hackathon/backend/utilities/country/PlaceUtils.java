package com.hackathon.backend.utilities.country;

import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlaceUtils {

    private final PlaceRepository placeRepository;
    private final S3Service s3Service;

    @Autowired
    public PlaceUtils(PlaceRepository placeRepository,
                      S3Service s3Service) {
        this.placeRepository = placeRepository;
        this.s3Service = s3Service;
    }

    public void save(PlaceEntity place) {
        placeRepository.save(place);
    }

    public PlaceEntity findById(int placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(()-> new EntityNotFoundException("Place id not found"));
    }

    public void delete(PlaceEntity place) {
        placeRepository.delete(place);
    }

    public List<GetEssentialPlaceDto> findPlacesByCountryId(int countryId){
        return placeRepository.findPlacesByCountryId(countryId);
    }

    public void deleteAll() {
        placeRepository.deleteAll();
    }

    public boolean checkHelper(EditPlaceDto editPlaceDto){
        return  editPlaceDto.getPlace() != null ||
                editPlaceDto.getMainImage() != null;
    }

    public void editHelper(PlaceEntity place,
                            EditPlaceDto editPlaceDto) {
        if(editPlaceDto.getPlace() != null){
            place.setPlace(editPlaceDto.getPlace());
        }
        if(editPlaceDto.getMainImage() != null){
            String placeMainImageName = s3Service.uploadFile(editPlaceDto.getMainImage());
            place.setMainImage(placeMainImageName);
        }
    }
}
