package com.hackathon.backend.dto.countryDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCountryWithCountryDetailsDto {
    private int id;
    private String country;
    private String countryMainImage;
    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String description;

    public GetCountryWithCountryDetailsDto(int id, String country,
                                           String countryMainImage,
                                           String imageOne, String imageTwo,
                                           String imageThree, String description) {
        this.id = id;
        this.country = country;
        this.countryMainImage = countryMainImage;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
    }

    @Override
    public String toString() {
        return "GetCountryWithCountryDetailsDto{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", countryMainImage='" + countryMainImage + '\'' +
                ", imageOne='" + imageOne + '\'' +
                ", imageTwo='" + imageTwo + '\'' +
                ", imageThree='" + imageThree + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
