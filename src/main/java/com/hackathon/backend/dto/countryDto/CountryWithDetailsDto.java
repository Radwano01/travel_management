package com.hackathon.backend.dto.countryDto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountryWithDetailsDto {
    private int id;
    private String country;
    private String mainImage;
    private CountryDetailsDto countryDetails;
}
