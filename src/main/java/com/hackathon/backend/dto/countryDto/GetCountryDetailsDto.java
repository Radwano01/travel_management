package com.hackathon.backend.dto.countryDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCountryDetailsDto {
    private int id;
    private String country;
    private String countryMainImage;
    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String description;

    public GetCountryDetailsDto(int id, String imageOne,
                                String imageTwo, String imageThree,
                                String description, String country,
                                String countryMainImage) {
        this.id = id;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
        this.country = country;
        this.countryMainImage = countryMainImage;
    }
}
