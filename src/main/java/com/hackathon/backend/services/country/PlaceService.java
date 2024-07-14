package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceForFlightDto;
import com.hackathon.backend.dto.countryDto.placeDto.PostPlaceDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.country.PlaceDetailsUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import com.hackathon.backend.utilities.hotel.HotelEvaluationUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.RoomUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PlaceService{

    private final CountryUtils countryUtils;
    private final PlaceUtils placeUtils;
    private final PlaceDetailsUtils placeDetailsUtils;
    private final S3Service s3Service;
    private final HotelUtils hotelUtils;
    private final RoomDetailsUtils roomDetailsUtils;
    private final HotelEvaluationUtils hotelEvaluationUtils;
    private final RoomUtils roomUtils;

    @Autowired
    public PlaceService(CountryUtils countryUtils,
                        PlaceUtils placeUtils,
                        PlaceDetailsUtils placeDetailsUtils,
                        S3Service s3Service, HotelUtils hotelUtils,
                        RoomDetailsUtils roomDetailsUtils,
                        HotelEvaluationUtils hotelEvaluationUtils,
                        RoomUtils roomUtils) {
        this.countryUtils = countryUtils;
        this.placeUtils = placeUtils;
        this.placeDetailsUtils = placeDetailsUtils;
        this.s3Service = s3Service;
        this.hotelUtils = hotelUtils;
        this.roomDetailsUtils = roomDetailsUtils;
        this.hotelEvaluationUtils = hotelEvaluationUtils;
        this.roomUtils = roomUtils;
    }

    public ResponseEntity<String> createPlace(int countryId, @NonNull PostPlaceDto postPlaceDto) {
        try {
            CountryEntity country = countryUtils.findCountryById(countryId);

            String placeImageName = s3Service.uploadFile(postPlaceDto.getMainImage());

            PlaceEntity place = new PlaceEntity(
                    postPlaceDto.getPlace(),
                    placeImageName,
                    country
            );

            String placeDetailsImageNameOne = s3Service.uploadFile(postPlaceDto.getImageOne());
            String placeDetailsImageNameTwo = s3Service.uploadFile(postPlaceDto.getImageTwo());
            String placeDetailsImageNameThree = s3Service.uploadFile(postPlaceDto.getImageThree());

            PlaceDetailsEntity placeDetails = new PlaceDetailsEntity(
                    placeDetailsImageNameOne,
                    placeDetailsImageNameTwo,
                    placeDetailsImageNameThree,
                    postPlaceDto.getDescription(),
                    place
            );

            placeDetailsUtils.save(placeDetails);
            place.setPlaceDetails(placeDetails);

            placeUtils.save(place);

            country.getPlaces().add(place);
            countryUtils.save(country);

            return ResponseEntity.ok("Place created successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getPlacesByCountryId(int countryId){
        try{
            List<GetEssentialPlaceDto> places = placeUtils.findPlacesByCountryId(countryId);
            return ResponseEntity.ok(places);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getPlaceByPlace(String place){
        try{
            List<GetPlaceForFlightDto> places = placeUtils.findPlaceByPlace(place);
            return ResponseEntity.ok(places);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> editPlace(int countryId,
                                       int placeId,
                                       EditPlaceDto editPlaceDto) {
        try{
            if(!placeUtils.checkHelper(editPlaceDto)){
                return badRequestException("you sent an empty data to change");
            }
            CountryEntity country = countryUtils.findCountryById(countryId);
            Optional<PlaceEntity> place = country.getPlaces().stream()
                    .filter((data)-> data.getId() == placeId).findFirst();
            if(place.isPresent()) {
                placeUtils.editHelper(place.get(), editPlaceDto);
                placeUtils.save(place.get());
                countryUtils.save(country);
                return ResponseEntity.ok("Place updated successfully");
            }else{
                return notFoundException("Place not found in country data");
            }
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> deletePlace(int countryId,
                                         int placeId) {
        try{
            CountryEntity country = countryUtils.findCountryById(countryId);
            Optional<PlaceEntity> place = country.getPlaces().stream()
                    .filter((data)-> data.getId() == placeId).findFirst();



            if(place.isPresent()) {
                if (place.get().getHotels() != null) {
                    for (HotelEntity hotel : place.get().getHotels()) {
                        RoomDetailsEntity roomDetails = hotel.getRoomDetails();

                        if (roomDetails.getHotelFeatures() != null) {
                            roomDetails.getHotelFeatures().clear();
                        }

                        if (roomDetails.getRoomFeatures() != null) {
                            roomDetails.getRoomFeatures().clear();
                        }
                        s3Service.deleteFile(roomDetails.getImageOne());
                        s3Service.deleteFile(roomDetails.getImageTwo());
                        s3Service.deleteFile(roomDetails.getImageThree());
                        s3Service.deleteFile(roomDetails.getImageFour());

                        roomDetailsUtils.delete(roomDetails);

                        List<HotelEvaluationEntity> hotelEvaluations = hotel.getEvaluations();
                        if (hotelEvaluations != null) {
                            for (HotelEvaluationEntity hotelEvaluation : hotelEvaluations) {
                                hotelEvaluationUtils.delete(hotelEvaluation);
                            }
                        }
                        if (hotel.getRooms() != null) {
                            for (RoomEntity room : hotel.getRooms()) {
                                roomUtils.delete(room);
                            }
                        }

                        s3Service.deleteFile(hotel.getMainImage());
                        hotelUtils.delete(hotel);
                    }
                }

                if(place.get().getPlaceDetails() != null) {
                    PlaceDetailsEntity placeDetails = place.get().getPlaceDetails();

                    s3Service.deleteFile(placeDetails.getImageOne());
                    s3Service.deleteFile(placeDetails.getImageTwo());
                    s3Service.deleteFile(placeDetails.getImageThree());
                    placeDetailsUtils.delete(placeDetails);
                }
                s3Service.deleteFile(place.get().getMainImage());
                placeUtils.delete(place.get());
                countryUtils.save(country);
                return ResponseEntity.ok("Place deleted successfully");
            }else{
                return notFoundException("Place not found in country data");
            }
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
