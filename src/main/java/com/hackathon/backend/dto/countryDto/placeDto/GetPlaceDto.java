package com.hackathon.backend.dto.countryDto.placeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetPlaceDto {

    private int id;
    private String place;
    private String mainImage;
    private GetPlaceDetailsDto placeDetails;
    // to edit place details
    private String country;
}
