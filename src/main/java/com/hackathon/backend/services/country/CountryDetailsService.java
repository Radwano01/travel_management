package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.CountryDetailsDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.utilities.country.CountryDetailsUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class CountryDetailsService {

    private final CountryDetailsUtils countryDetailsUtils;

    @Autowired
    public CountryDetailsService(CountryDetailsUtils countryDetailsUtils) {
        this.countryDetailsUtils = countryDetailsUtils;
    }

    public ResponseEntity<?> getSingleCountryDetails(int countryId) {
        try {
            CountryDetailsEntity countryDetails = countryDetailsUtils.findByCountryId(countryId);

            CountryDetailsDto countryDetailsDto = new CountryDetailsDto(
                    countryDetails.getId(),
                    countryDetails.getImageOne(),
                    countryDetails.getImageTwo(),
                    countryDetails.getImageThree(),
                    countryDetails.getDescription(),
                    countryDetails.getCountry().getCountry(),
                    countryDetails.getCountry().getMainImage()
            );
            return ResponseEntity.ok(countryDetailsDto);
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editCountryDetails(int countryDetailsId,
                                                CountryDetailsDto countryDetailsDto) {
        try {
            CountryDetailsEntity countryDetails = countryDetailsUtils.findById(countryDetailsId);
            editHelper(countryDetails, countryDetailsDto);
            countryDetailsUtils.save(countryDetails);
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
            countryDetails.setImageOne(countryDetails.getImageOne());
        }
        if(countryDetailsDto.getImageTwo() != null){
            countryDetails.setImageTwo(countryDetails.getImageTwo());
        }
        if(countryDetailsDto.getImageThree() != null){
            countryDetails.setImageThree(countryDetails.getImageThree());
        }
        if(countryDetailsDto.getDescription() != null){
            countryDetails.setDescription(countryDetails.getDescription());
        }
    }
}


