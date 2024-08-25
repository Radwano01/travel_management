package com.hackathon.backend.services.country.impl;

import com.hackathon.backend.dto.countryDto.placeDto.CreatePlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceForFlightDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.services.country.PlaceService;
import com.hackathon.backend.utilities.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;


import java.util.List;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PlaceServiceImpl implements PlaceService {

    private final CountryRepository countryRepository;
    private final PlaceRepository placeRepository;
    private final S3Service s3Service;

    @Autowired
    public PlaceServiceImpl(CountryRepository countryRepository,
                            PlaceRepository placeRepository,
                            S3Service s3Service) {
        this.countryRepository = countryRepository;
        this.placeRepository = placeRepository;
        this.s3Service = s3Service;
    }

    @Transactional
    @Override
    public ResponseEntity<String> createPlace(int countryId, @NonNull CreatePlaceDto createPlaceDto) {
        CountryEntity country = getCountryById(countryId);

        PlaceEntity place = savePlaceInCountry(createPlaceDto, country);

        prepareANDSetPlaceDetails(createPlaceDto, place);

        countryRepository.save(country);

        return ResponseEntity.ok(place.toString());
    }

    private PlaceEntity savePlaceInCountry(CreatePlaceDto createPlaceDto, CountryEntity country){
        String placeImageName = s3Service.uploadFile(createPlaceDto.getMainImage());

        PlaceEntity place = new PlaceEntity(
                createPlaceDto.getPlace(),
                placeImageName,
                country
        );

        country.getPlaces().add(place);

        return place;
    }

    private void prepareANDSetPlaceDetails(CreatePlaceDto createPlaceDto, PlaceEntity place){
        String placeDetailsImageNameOne = s3Service.uploadFile(createPlaceDto.getImageOne());
        String placeDetailsImageNameTwo = s3Service.uploadFile(createPlaceDto.getImageTwo());
        String placeDetailsImageNameThree = s3Service.uploadFile(createPlaceDto.getImageThree());

        PlaceDetailsEntity placeDetails = new PlaceDetailsEntity(
                placeDetailsImageNameOne,
                placeDetailsImageNameTwo,
                placeDetailsImageNameThree,
                createPlaceDto.getDescription(),
                place
        );

        place.setPlaceDetails(placeDetails);
    }

    private CountryEntity getCountryById(int countryId){
        return countryRepository.findById(countryId)
                .orElseThrow(()-> new EntityNotFoundException("No such country has this id."));
    }

    @Override
    public ResponseEntity<List<GetEssentialPlaceDto>> getAllPlacesByCountryId(int countryId) {
           return ResponseEntity.ok(countryRepository.findEssentialPlacesDataByCountryId(countryId));
    }

    @Override
    public ResponseEntity<List<GetPlaceForFlightDto>> getPlaceByPlace(String place) {
        return ResponseEntity.ok(placeRepository.findPlaceByPlace(place));
    }

    @Transactional
    @Override
    public ResponseEntity<String> editPlace(int countryId, int placeId, EditPlaceDto editPlaceDto) {
        if(!checkIfSentEmptyData(editPlaceDto)){
            return badRequestException("you sent an empty data to change");
        }

        CountryEntity country = getCountryById(countryId);

        PlaceEntity place = getPlaceByCountryIdANDPlaceId(countryId, placeId);

        updateToNewData(place, editPlaceDto);

        countryRepository.save(country);

        return ResponseEntity.ok(place.toString());
    }

    private PlaceEntity getPlaceByCountryIdANDPlaceId(int countryId, int placeId) {
        return countryRepository.findPlaceByCountryIdANDPlaceId(countryId, placeId)
                .orElseThrow(()-> new EntityNotFoundException("Place id not found"));
    }

    private void updateToNewData(PlaceEntity place, EditPlaceDto editPlaceDto) {
        if(editPlaceDto.getPlace() != null){
            place.setPlace(editPlaceDto.getPlace());
        }
        if(editPlaceDto.getMainImage() != null){
            s3Service.deleteFile(place.getMainImage());
            String placeMainImageName = s3Service.uploadFile(editPlaceDto.getMainImage());
            place.setMainImage(placeMainImageName);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> deletePlace(int countryId, int placeId) {
        CountryEntity country = getCountryById(countryId);

        PlaceEntity place = getPlaceByCountryIdANDPlaceId(countryId, placeId);

        deleteHotelANDRoomDetailsImages(place);

        deletePlaceANDPlaceDetailsImages(place);

        countryRepository.save(country);

        return ResponseEntity.ok("Place deleted successfully");
    }


    private void deleteHotelANDRoomDetailsImages(PlaceEntity place){
        if(place.getHotels() != null) {
            for (HotelEntity hotel : place.getHotels()) {
                RoomDetailsEntity roomDetails = hotel.getRoomDetails();

                s3Service.deleteFile(hotel.getMainImage());

                s3Service.deleteFile(roomDetails.getImageOne());
                s3Service.deleteFile(roomDetails.getImageTwo());
                s3Service.deleteFile(roomDetails.getImageThree());
                s3Service.deleteFile(roomDetails.getImageFour());
            }
        }
    }

    private void deletePlaceANDPlaceDetailsImages(PlaceEntity place){
        s3Service.deleteFile(place.getMainImage());

        s3Service.deleteFile(place.getPlaceDetails().getImageOne());
        s3Service.deleteFile(place.getPlaceDetails().getImageTwo());
        s3Service.deleteFile(place.getPlaceDetails().getImageThree());
    }
}
