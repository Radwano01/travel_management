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

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class CountryDetailsService {

    private final CountryUtils countryUtils;
    private final CountryDetailsUtils countryDetailsUtils;

    @Autowired
    public CountryDetailsService(CountryUtils countryUtils,
                                 CountryDetailsUtils countryDetailsUtils) {
        this.countryUtils = countryUtils;
        this.countryDetailsUtils = countryDetailsUtils;
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
    public ResponseEntity<String> editCountryDetails(int countryId,
                                                    EditCountryDetailsDto editCountryDetailsDto) {
        try {
            if(!countryDetailsUtils.checkHelper(editCountryDetailsDto)){
                return badRequestException("you sent an empty data to change");
            }
            CountryEntity country = countryUtils.findCountryById(countryId);
            countryDetailsUtils.editHelper(country.getCountryDetails(), editCountryDetailsDto);
            countryUtils.save(country);
            countryDetailsUtils.save(country.getCountryDetails());
            return ResponseEntity.ok("Country details updated successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}


