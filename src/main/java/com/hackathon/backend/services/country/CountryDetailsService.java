package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.EditCountryDetailsDto;
import com.hackathon.backend.dto.countryDto.GetCountryWithCountryDetailsDto;
import org.springframework.http.ResponseEntity;

public interface CountryDetailsService {
    ResponseEntity<GetCountryWithCountryDetailsDto> getSingleCountryDetails(int countryId);

    ResponseEntity<String> editCountryDetails(int countryId, EditCountryDetailsDto editCountryDetailsDto);
}
