package com.hackathon.backend.dto.hotelDto.features;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetHotelFeaturesDto {
    private int id;
    private String hotelFeature;

    public GetHotelFeaturesDto(int id, String hotelFeature){
        this.id = id;
        this.hotelFeature = hotelFeature;
    }
}
