package com.hackathon.backend.services.country;

import com.hackathon.backend.dto.countryDto.placeDto.CreatePlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceForFlightDto;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PlaceService {

    ResponseEntity<String> createPlace(int countryId, @NonNull CreatePlaceDto createPlaceDto);

    ResponseEntity<List<GetEssentialPlaceDto>> getAllPlacesByCountryId(int countryId);

    ResponseEntity<List<GetPlaceForFlightDto>> getPlaceByPlace(String place);

    ResponseEntity<String> editPlace(int countryId, int placeId, EditPlaceDto editPlaceDto);

    ResponseEntity<String> deletePlace(int countryId, int placeId);
}
