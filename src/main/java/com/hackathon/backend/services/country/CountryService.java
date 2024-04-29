package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.CountryDto;
import com.hackathon.backend.dto.countryDto.CountryWithDetailsDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.utilities.country.CountryDetailsUtils;
import com.hackathon.backend.utilities.country.CountryUtils;
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
public class CountryService {

    private final CountryUtils countryUtils;
    private final CountryDetailsUtils countryDetailsUtils;
    @Autowired
    public CountryService(CountryUtils countryUtils,
                          CountryDetailsUtils countryDetailsUtils) {
        this.countryUtils = countryUtils;
        this.countryDetailsUtils = countryDetailsUtils;
    }

    public ResponseEntity<?> createCountry(@NonNull CountryWithDetailsDto countryWithDetailsDto) {
        try {
            String countryName = countryWithDetailsDto.getCountry().trim();
            boolean existsCountry = countryUtils.existsByCountry(countryName);
            if (existsCountry) {
                return alreadyValidException("Country already exist: "+countryName);
            }
            CountryEntity country = new CountryEntity(
                    countryWithDetailsDto.getCountry(),
                    countryWithDetailsDto.getMainImage()
            );
            countryUtils.save(country);
            CountryDetailsEntity countryDetails = new CountryDetailsEntity(
                    countryWithDetailsDto.getCountryDetails().getImageOne(),
                    countryWithDetailsDto.getCountryDetails().getImageTwo(),
                    countryWithDetailsDto.getCountryDetails().getImageThree(),
                    countryWithDetailsDto.getCountryDetails().getDescription(),
                    country
            );
            countryDetailsUtils.save(countryDetails);
            country.setCountryDetails(countryDetails);
            return ResponseEntity.ok("Country created Successfully: "+countryName);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getCountries() {
        try {

            List<CountryDto> countries = countryUtils.findAllCountries();
            return ResponseEntity.ok(countries);
        }catch (EmptyResultDataAccessException e) {
            return notFoundException("No country added yet!");
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editCountry(int countryId,
                                         CountryDto countryDto) {
        try {
            CountryEntity country = countryUtils.findCountryById(countryId);
            editHelper(country,countryDto);
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
            CountryDetailsEntity countryDetails = countryDetailsUtils.findByCountryId(countryId);
            if(countryEntity != null && countryDetails != null){
                countryDetailsUtils.delete(countryDetails);
                countryUtils.delete(countryEntity);
                return ResponseEntity.ok("Country and country details deleted successfully");
            }else{
                return notFoundException("Country or country details not found");
            }
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    private void editHelper(CountryEntity country,
                            CountryDto countryDto) {
        if (countryDto.getCountry() != null) {
            country.setCountry(countryDto.getCountry());
        }
        if (countryDto.getMainImage() != null) {
            country.setMainImage(countryDto.getMainImage());
        }
    }
}
