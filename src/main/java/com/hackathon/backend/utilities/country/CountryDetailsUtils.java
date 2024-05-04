package com.hackathon.backend.utilities.country;

import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.repositories.country.CountryDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CountryDetailsUtils {

    private final CountryDetailsRepository countryDetailsRepository;

    @Autowired
    public CountryDetailsUtils(CountryDetailsRepository countryDetailsRepository) {
        this.countryDetailsRepository = countryDetailsRepository;
    }

    public void save(CountryDetailsEntity countryDetails) {
        countryDetailsRepository.save(countryDetails);
    }

    public CountryDetailsEntity findByCountryId(int countryId) {
        return countryDetailsRepository.findByCountryId(countryId)
                .orElseThrow(()-> new EntityNotFoundException("Country id not found"));
    }

    public void delete(CountryDetailsEntity countryDetails) {
        countryDetailsRepository.delete(countryDetails);
    }

    public CountryDetailsEntity findById(int countryDetailsId) {
        return countryDetailsRepository.findById(countryDetailsId)
                .orElseThrow(() -> new EntityNotFoundException("Country details id not found"));
    }

    public void deleteAll() {
        countryDetailsRepository.deleteAll();
    }
}
