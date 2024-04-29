package com.hackathon.backend.utilities.country;


import com.hackathon.backend.dto.countryDto.CountryDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountryUtils {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryUtils(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public CountryEntity findCountryById(@NonNull int countryId){
        return countryRepository.findById(countryId)
                .orElseThrow(()-> new EntityNotFoundException("Country id is not found"));
    }

    public boolean existsByCountry(String countryName) {
        return countryRepository.existsByCountry(countryName);
    }

    public void save(CountryEntity country) {
        countryRepository.save(country);
    }

    public List<CountryDto> findAllCountries() {
        return countryRepository.findAllCountries();
    }

    public void delete(CountryEntity countryEntity) {
        countryRepository.delete(countryEntity);
    }
}
