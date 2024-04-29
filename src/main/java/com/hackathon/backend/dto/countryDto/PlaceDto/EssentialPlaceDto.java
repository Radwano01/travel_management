package com.hackathon.backend.dto.countryDto.PlaceDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EssentialPlaceDto {
    private int id;
    private String place;
    private String mainImage;

    public EssentialPlaceDto(int id, String place, String mainImage) {
        this.id = id;
        this.place = place;
        this.mainImage = mainImage;
    }
}
