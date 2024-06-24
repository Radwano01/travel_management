package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.*;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.hotel.hotelFeatures.HotelFeaturesRepository;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryDetailsUtils;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.country.PlaceDetailsUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import com.hackathon.backend.utilities.hotel.HotelEvaluationUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.RoomUtils;
import com.hackathon.backend.utilities.hotel.features.HotelFeaturesUtils;
import com.hackathon.backend.utilities.hotel.features.RoomFeaturesUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class CountryService{

    private final CountryUtils countryUtils;
    private final CountryDetailsUtils countryDetailsUtils;
    private final PlaceUtils placeUtils;
    private final PlaceDetailsUtils placeDetailsUtils;
    private final HotelUtils hotelUtils;
    private final RoomDetailsUtils roomDetailsUtils;
    private final HotelEvaluationUtils hotelEvaluationUtils;
    private final RoomUtils roomUtils;
    private final HotelFeaturesUtils hotelFeaturesUtils;
    private final RoomFeaturesUtils roomFeaturesUtils;
    private final PackageUtils packageUtils;
    private final PlaneFlightsUtils planeFlightsUtils;
    private final S3Service s3Service;

    @Autowired
    public CountryService(CountryUtils countryUtils,
                          CountryDetailsUtils countryDetailsUtils,
                          PlaceUtils placeUtils,
                          PlaceDetailsUtils placeDetailsUtils,
                          HotelUtils hotelUtils,
                          RoomDetailsUtils roomDetailsUtils,
                          HotelEvaluationUtils hotelEvaluationUtils,
                          RoomUtils roomUtils,
                          HotelFeaturesUtils hotelFeaturesUtils,
                          RoomFeaturesUtils roomFeaturesUtils,
                          PackageUtils packageUtils,
                          PlaneFlightsUtils planeFlightsUtils,
                          S3Service s3Service) {
        this.countryUtils = countryUtils;
        this.countryDetailsUtils = countryDetailsUtils;
        this.placeUtils = placeUtils;
        this.placeDetailsUtils = placeDetailsUtils;
        this.hotelUtils = hotelUtils;
        this.roomDetailsUtils = roomDetailsUtils;
        this.hotelEvaluationUtils = hotelEvaluationUtils;
        this.roomUtils = roomUtils;
        this.hotelFeaturesUtils = hotelFeaturesUtils;
        this.roomFeaturesUtils = roomFeaturesUtils;
        this.packageUtils = packageUtils;
        this.planeFlightsUtils = planeFlightsUtils;
        this.s3Service = s3Service;
    }

    public ResponseEntity<?> createCountry(@NonNull PostCountryDto postCountryDto) {
        try {
            String countryName = postCountryDto.getCountry().trim().toLowerCase();
            boolean existsCountry = countryUtils.existsByCountry(countryName);
            if (existsCountry) {
                return alreadyValidException("Country already exist: "+countryName);
            }

            String countryImageName = s3Service.uploadFile(postCountryDto.getMainImage());

            CountryEntity country = new CountryEntity(
                    postCountryDto.getCountry(),
                    countryImageName
            );
            countryUtils.save(country);

            String countryDetailsImageNameOne = s3Service.uploadFile(postCountryDto.getImageOne());
            String countryDetailsImageNameTwo = s3Service.uploadFile(postCountryDto.getImageTwo());
            String countryDetailsImageNameThree = s3Service.uploadFile(postCountryDto.getImageThree());

            CountryDetailsEntity countryDetails = new CountryDetailsEntity(
                    countryDetailsImageNameOne,
                    countryDetailsImageNameTwo,
                    countryDetailsImageNameThree,
                    postCountryDto.getDescription(),
                    country
            );
            countryDetailsUtils.save(countryDetails);
            country.setCountryDetails(countryDetails);
            countryUtils.save(country);
            return ResponseEntity.ok("Country created Successfully: "+countryName);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getCountry() {
        try {
            List<GetCountryDto> countries = countryUtils.findAllCountries();
            return ResponseEntity.ok(countries);
        }catch (EmptyResultDataAccessException e) {
            return notFoundException("No country added yet!");
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editCountry(int countryId,
                                        EditCountryDto editCountryDto) {
        try {
            CountryEntity country = countryUtils.findCountryById(countryId);
            editHelper(country, editCountryDto);
            countryUtils.save(country);
            return ResponseEntity.ok("Country edited successfully to: " + country.getCountry());
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteCountry(int countryId) {
        try {
            CountryEntity countryEntity = countryUtils.findCountryById(countryId);

            for(PlaceEntity place:countryEntity.getPlaces()){
                PlaceDetailsEntity placeDetails = place.getPlaceDetails();
                if(placeDetails != null){
                    placeDetailsUtils.delete(placeDetails);
                }
                placeUtils.delete(place);
            }

            for(HotelEntity hotel:countryEntity.getHotels()){
                RoomDetailsEntity roomDetails = hotel.getRoomDetails();
                if(roomDetails != null){
                    roomDetailsUtils.delete(roomDetails);
                }
                List<HotelEvaluationEntity> hotelEvaluation = hotel.getEvaluations();
                if(hotelEvaluation != null){
                    for (HotelEvaluationEntity hotelEvaluationEntity : hotelEvaluation) {
                        hotelEvaluationUtils.delete(hotelEvaluationEntity);
                    }
                }
                if(hotel.getRooms() != null){
                    for(RoomEntity room:hotel.getRooms()){
                        roomUtils.delete(room);
                    }
                }
                hotelUtils.delete(hotel);
            }

            for(PackageEntity packageEntity:countryEntity.getPackages()){
                packageUtils.delete(packageEntity);
            }

            for(PlaneFlightsEntity departure:countryEntity.getDepartingFlights()){
                planeFlightsUtils.delete(departure);
            }

            for(PlaneFlightsEntity arrival:countryEntity.getArrivingFlights()){
                planeFlightsUtils.delete(arrival);
            }

            CountryDetailsEntity countryDetails = countryEntity.getCountryDetails();
            String[] ls = new String[]{
                countryEntity.getMainImage(),
                countryDetails.getImageOne(),
                countryDetails.getImageTwo(),
                countryDetails.getImageThree()
            };
            s3Service.deleteFiles(ls);
            countryDetailsUtils.delete(countryEntity.getCountryDetails());
            s3Service.deleteFile(countryEntity.getMainImage());
            countryUtils.delete(countryEntity);
            return ResponseEntity.ok("Country and country details deleted successfully");
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    private void editHelper(CountryEntity country,
                            EditCountryDto editCountryDto) {
        if (editCountryDto.getCountry() != null) {
            boolean existsCountry = countryUtils.existsByCountry(editCountryDto.getCountry());
            if(!existsCountry) country.setCountry(editCountryDto.getCountry());
        }
        if (editCountryDto.getMainImage() != null) {
            s3Service.deleteFile(country.getMainImage());
            String newMainImageName = s3Service.uploadFile(editCountryDto.getMainImage());
            country.setMainImage(newMainImageName);
        }
    }
}
