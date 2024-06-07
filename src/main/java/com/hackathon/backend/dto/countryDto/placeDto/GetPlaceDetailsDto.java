package com.hackathon.backend.dto.countryDto.placeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetPlaceDetailsDto {
    private int id;
    private String place;
    private String mainImage;
    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String description;
    //add packages variable soon!!

    public GetPlaceDetailsDto(int id, String place,
                              String mainImage, String imageOne,
                              String imageTwo, String imageThree,
                              String description) {
        this.id = id;
        this.place = place;
        this.mainImage = mainImage;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
    }

}
