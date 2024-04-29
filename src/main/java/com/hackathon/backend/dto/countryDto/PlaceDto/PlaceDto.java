package com.hackathon.backend.dto.countryDto.PlaceDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaceDto {

    private int id;
    private String place;
    private String mainImage;
    private PlaceDetailsDto placeDetails;
    // to edit place details
    private String country;
}
