package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.*;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.utilities.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class CountryService{

    private final CountryRepository countryRepository;
    private final S3Service s3Service;

    @Autowired
    public CountryService(CountryRepository countryRepository,
                          S3Service s3Service) {
        this.countryRepository = countryRepository;
        this.s3Service = s3Service;
    }


    @Transactional
    public ResponseEntity<String> createCountry(@NonNull CreateCountryDto createCountryDto) {
        ResponseEntity<String> checkResult = validateCountryExistence(createCountryDto.getCountry());
        if(!checkResult.getStatusCode().equals(HttpStatus.OK)){
            return checkResult;
        }

        CountryEntity country = saveCountryInDB(createCountryDto);

        prepareANDSetCountryDetails(createCountryDto, country);

        //save country details in country because there is composition relationship between them.
        updateCountryToSaveCountryDetailsIn(country);

        return ResponseEntity.ok(country.toString());
    }

    private CountryEntity saveCountryInDB(CreateCountryDto createCountryDto){
        String countryImageName = s3Service.uploadFile(createCountryDto.getMainImage());

        CountryEntity country = new CountryEntity(
                createCountryDto.getCountry(),
                countryImageName
        );

        countryRepository.save(country);

        return country;
    }

    private void prepareANDSetCountryDetails(CreateCountryDto createCountryDto, CountryEntity country){
        String countryDetailsImageNameOne = s3Service.uploadFile(createCountryDto.getImageOne());
        String countryDetailsImageNameTwo = s3Service.uploadFile(createCountryDto.getImageTwo());
        String countryDetailsImageNameThree = s3Service.uploadFile(createCountryDto.getImageThree());

        CountryDetailsEntity countryDetails = new CountryDetailsEntity(
                countryDetailsImageNameOne,
                countryDetailsImageNameTwo,
                countryDetailsImageNameThree,
                createCountryDto.getDescription(),
                country
        );

        country.setCountryDetails(countryDetails);
    }

    private void updateCountryToSaveCountryDetailsIn(CountryEntity country){
        countryRepository.save(country);
    }

    private CountryEntity getCountryById(int countryId){
        return countryRepository.findById(countryId)
                .orElseThrow(()-> new EntityNotFoundException("No such country has this id."));
    }

    private ResponseEntity<String> validateCountryExistence(String countryName) {
        if (countryRepository.existsByCountry(countryName)) {
            return alreadyValidException("Country already exists: " + countryName);
        }
        return ResponseEntity.ok("OK");
    }

    public ResponseEntity<List<GetCountryDto>> getAllCountries() {
        return ResponseEntity.ok(countryRepository.findAllCountries());
    }

    @Transactional
    public ResponseEntity<String> editCountry(int countryId, EditCountryDto editCountryDto) {
        if(!checkIfSentEmptyData(editCountryDto)){
            return badRequestException("you sent an empty data to change");
        }

        CountryEntity country = getCountryById(countryId);

        updateToNewData(country, editCountryDto);

        countryRepository.save(country);

        return ResponseEntity.ok(country.toString());
    }

    private void updateToNewData(CountryEntity country, EditCountryDto editCountryDto) {
        if (editCountryDto.getCountry() != null) {
            boolean existsCountry = countryRepository.existsByCountry(editCountryDto.getCountry());
            if(!existsCountry) country.setCountry(editCountryDto.getCountry());
        }
        if (editCountryDto.getMainImage() != null) {
            s3Service.deleteFile(country.getMainImage());
            String newMainImageName = s3Service.uploadFile(editCountryDto.getMainImage());
            country.setMainImage(newMainImageName);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteCountry(int countryId) {
        CountryEntity countryEntity = getCountryById(countryId);

        deletePlacesANDPlaceDetailsImages(countryEntity);

        deletePackagesANDPackageDetailsImages(countryEntity);

        deleteCountryANDCountryDetailsImages(countryEntity);

        countryRepository.delete(countryEntity);

        return ResponseEntity.ok("Country deleted successfully");
    }



    private void deleteCountryANDCountryDetailsImages(CountryEntity country){
        s3Service.deleteFile(country.getMainImage());

        if(country.getCountryDetails() != null) {
            s3Service.deleteFile(country.getCountryDetails().getImageOne());
            s3Service.deleteFile(country.getCountryDetails().getImageTwo());
            s3Service.deleteFile(country.getCountryDetails().getImageThree());
        }
    }

    private void deletePlacesANDPlaceDetailsImages(CountryEntity countryEntity){
        if (countryEntity.getPlaces() != null) {
            for (PlaceEntity place : countryEntity.getPlaces()) {
                PlaceDetailsEntity placeDetails = place.getPlaceDetails();
                if (placeDetails != null) {
                    s3Service.deleteFile(placeDetails.getImageOne());
                    s3Service.deleteFile(placeDetails.getImageTwo());
                    s3Service.deleteFile(placeDetails.getImageThree());
                }
                s3Service.deleteFile(place.getMainImage());
            }
        }
    }

    private void deletePackagesANDPackageDetailsImages(CountryEntity countryEntity){
        if (countryEntity.getPackages() != null) {
            for (PackageEntity packageEntity : countryEntity.getPackages()) {
                PackageDetailsEntity packageDetails = packageEntity.getPackageDetails();

                s3Service.deleteFile(packageDetails.getImageOne());
                s3Service.deleteFile(packageDetails.getImageTwo());
                s3Service.deleteFile(packageDetails.getImageThree());

                s3Service.deleteFile(packageEntity.getMainImage());
            }
        }
    }
}
