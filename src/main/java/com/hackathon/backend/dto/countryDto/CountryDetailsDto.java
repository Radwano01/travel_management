package com.hackathon.backend.dto.countryDto;

import com.hackathon.backend.dto.countryDto.PlaceDto.EssentialPlaceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CountryDetailsDto {
    private int id;
    private String country;
    private String countryMainImage;
    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String description;
    private List<EssentialPlaceDto> places;

    public CountryDetailsDto(int id, String imageOne,
                             String imageTwo, String imageThree,
                             String description, String country,
                             String countryMainImage,
                             List<EssentialPlaceDto> places) {
        this.id = id;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
        this.country = country;
        this.countryMainImage = countryMainImage;
        this.places = places;
    }
}
