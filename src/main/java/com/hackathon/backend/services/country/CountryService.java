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
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
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
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
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
    private final PackageUtils packageUtils;
    private final PackageDetailsUtils packageDetailsUtils;
    private final S3Service s3Service;

    @Autowired
    public CountryService(CountryUtils countryUtils,
                          CountryDetailsUtils countryDetailsUtils,
                          PlaceUtils placeUtils,
                          PlaceDetailsUtils placeDetailsUtils,
                          PackageUtils packageUtils,
                          PackageDetailsUtils packageDetailsUtils,
                          S3Service s3Service) {
        this.countryUtils = countryUtils;
        this.countryDetailsUtils = countryDetailsUtils;
        this.placeUtils = placeUtils;
        this.placeDetailsUtils = placeDetailsUtils;
        this.packageUtils = packageUtils;
        this.packageDetailsUtils = packageDetailsUtils;
        this.s3Service = s3Service;
    }

    public ResponseEntity<String> createCountry(@NonNull PostCountryDto postCountryDto) {
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
    public ResponseEntity<String> editCountry(int countryId,
                                        EditCountryDto editCountryDto) {
        try {
            if(!countryUtils.checkHelper(editCountryDto)) {
                return badRequestException("you sent an empty data to change");
            }
            CountryEntity country = countryUtils.findCountryById(countryId);
            countryUtils.editHelper(country, editCountryDto);
            countryUtils.save(country);
            return ResponseEntity.ok("Country edited successfully to: " + country.getCountry());
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteCountry(int countryId) {
        try {
            CountryEntity countryEntity = countryUtils.findCountryById(countryId);

            if (countryEntity.getPlaces() != null) {
                for (PlaceEntity place : countryEntity.getPlaces()) {
                    PlaceDetailsEntity placeDetails = place.getPlaceDetails();
                    if (placeDetails != null) {
                        s3Service.deleteFile(placeDetails.getImageOne());
                        s3Service.deleteFile(placeDetails.getImageTwo());
                        s3Service.deleteFile(placeDetails.getImageThree());
                        placeDetailsUtils.delete(placeDetails);
                    }
                    s3Service.deleteFile(place.getMainImage());
                    placeUtils.delete(place);
                }
            }

            if (countryEntity.getPackages() != null) {
                for (PackageEntity packageEntity : countryEntity.getPackages()) {
                    PackageDetailsEntity packageDetails = packageEntity.getPackageDetails();

                    s3Service.deleteFile(packageDetails.getImageOne());
                    s3Service.deleteFile(packageDetails.getImageTwo());
                    s3Service.deleteFile(packageDetails.getImageThree());
                    packageDetailsUtils.delete(packageDetails);

                    s3Service.deleteFile(packageEntity.getMainImage());
                    packageUtils.delete(packageEntity);
                }
            }

            CountryDetailsEntity countryDetails = countryEntity.getCountryDetails();
            if (countryDetails != null) {
                s3Service.deleteFile(countryDetails.getImageOne());
                s3Service.deleteFile(countryDetails.getImageTwo());
                s3Service.deleteFile(countryDetails.getImageThree());
                countryDetailsUtils.delete(countryDetails);
            }

            s3Service.deleteFile(countryEntity.getMainImage());
            countryUtils.delete(countryEntity);

            return ResponseEntity.ok("Country and country details deleted successfully");
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}
