package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.CountryDetailsDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
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

    @Autowired
    public CountryDetailsService(CountryUtils countryUtils,
                                 CountryDetailsUtils countryDetailsUtils) {
        this.countryUtils = countryUtils;
        this.countryDetailsUtils = countryDetailsUtils;
    }

    public ResponseEntity<?> getSingleCountryDetails(int countryId) {
        try {
            CountryEntity country = countryUtils.findCountryById(countryId);

            CountryDetailsDto countryDetailsDto = new CountryDetailsDto(
                    country.getId(),
                    country.getCountryDetails().getImageOne(),
                    country.getCountryDetails().getImageTwo(),
                    country.getCountryDetails().getImageThree(),
                    country.getCountryDetails().getDescription(),
                    country.getCountry(),
                    country.getMainImage()
            );
            return ResponseEntity.ok(countryDetailsDto);
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editCountryDetails(int countryId,
                                                CountryDetailsDto countryDetailsDto) {
        try {
            CountryEntity country = countryUtils.findCountryById(countryId);
            editHelper(country.getCountryDetails(), countryDetailsDto);
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
                            CountryDetailsDto countryDetailsDto) {
        if(countryDetailsDto.getImageOne() != null){
            countryDetails.setImageOne(countryDetailsDto.getImageOne());
        }
        if(countryDetailsDto.getImageTwo() != null){
            countryDetails.setImageTwo(countryDetailsDto.getImageTwo());
        }
        if(countryDetailsDto.getImageThree() != null){
            countryDetails.setImageThree(countryDetailsDto.getImageThree());
        }
        if(countryDetailsDto.getDescription() != null){
            countryDetails.setDescription(countryDetailsDto.getDescription());
        }
    }
}


