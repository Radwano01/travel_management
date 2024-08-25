package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDetailsDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceDetailsDto;
import org.springframework.http.ResponseEntity;

public interface PlaceDetailsService {

    ResponseEntity<GetPlaceDetailsDto> getSinglePlaceDetails(int placeId);

    ResponseEntity<String> editPlaceDetails(int placeId, EditPlaceDetailsDto editPlaceDetailsDto);
}
