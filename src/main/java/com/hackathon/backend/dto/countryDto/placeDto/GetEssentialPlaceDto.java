package com.hackathon.backend.dto.countryDto.placeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetEssentialPlaceDto {
    private int id;
    private String place;
    private String mainImage;

    public GetEssentialPlaceDto(int id, String place, String mainImage) {
        this.id = id;
        this.place = place;
        this.mainImage = mainImage;
    }
}