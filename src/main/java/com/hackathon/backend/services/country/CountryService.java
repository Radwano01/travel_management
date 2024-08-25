package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.CreateCountryDto;
import com.hackathon.backend.dto.countryDto.EditCountryDto;
import com.hackathon.backend.dto.countryDto.GetCountryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CountryService {
    ResponseEntity<String> createCountry(@NonNull CreateCountryDto createCountryDto);

    ResponseEntity<List<GetCountryDto>> getAllCountries();

    ResponseEntity<String> editCountry(int countryId, EditCountryDto editCountryDto);

    ResponseEntity<String> deleteCountry(int countryId);
}
