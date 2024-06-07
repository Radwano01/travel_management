package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.EditCountryDetailsDto;
import com.hackathon.backend.dto.countryDto.GetCountryDetailsDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryDetailsUtils;
import com.hackathon.backend.utilities.country.CountryUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class CountryDetailsService {

    private final CountryUtils countryUtils;
    private final CountryDetailsUtils countryDetailsUtils;
    private final S3Service s3Service;

    @Autowired
    public CountryDetailsService(CountryUtils countryUtils,
                                 CountryDetailsUtils countryDetailsUtils,
                                 S3Service s3Service) {
        this.countryUtils = countryUtils;
        this.countryDetailsUtils = countryDetailsUtils;
        this.s3Service = s3Service;
    }

    public ResponseEntity<?> getSingleCountryDetails(int countryId) {
        try {
            CountryEntity country = countryUtils.findCountryById(countryId);

            GetCountryDetailsDto getCountryDetailsDto = new GetCountryDetailsDto(
                    country.getId(),
                    country.getCountryDetails().getImageOne(),
                    country.getCountryDetails().getImageTwo(),
                    country.getCountryDetails().getImageThree(),
                    country.getCountryDetails().getDescription(),
                    country.getCountry(),
                    country.getMainImage()
            );
            return ResponseEntity.ok(getCountryDetailsDto);
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editCountryDetails(int countryId,
                                                EditCountryDetailsDto editCountryDetailsDto) {
        try {
            CountryEntity country = countryUtils.findCountryById(countryId);
            editHelper(country.getCountryDetails(), editCountryDetailsDto);
            countryUtils.save(country);
            countryDetailsUtils.save(country.getCountryDetails());
            return ResponseEntity.ok("Country details updated successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    private void editHelper(CountryDetailsEntity countryDetails,
                            EditCountryDetailsDto editCountryDetailsDto) {
        if(editCountryDetailsDto.getImageOne() != null){
            s3Service.deleteFile(countryDetails.getImageOne());
            String countryDetailsImageOneName = s3Service.uploadFile(editCountryDetailsDto.getImageOne());
            countryDetails.setImageOne(countryDetailsImageOneName);
        }
        if(editCountryDetailsDto.getImageTwo() != null){
            s3Service.deleteFile(countryDetails.getImageTwo());
            String countryDetailsImageTwoName = s3Service.uploadFile(editCountryDetailsDto.getImageTwo());
            countryDetails.setImageTwo(countryDetailsImageTwoName);
        }
        if(editCountryDetailsDto.getImageThree() != null){
            s3Service.deleteFile(countryDetails.getImageThree());
            String countryDetailsImageThreeName = s3Service.uploadFile(editCountryDetailsDto.getImageThree());
            countryDetails.setImageThree(countryDetailsImageThreeName);
        }
        if(editCountryDetailsDto.getDescription() != null){
            countryDetails.setDescription(editCountryDetailsDto.getDescription());
        }
    }
}


