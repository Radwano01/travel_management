package com.hackathon.backend.dto.countryDto.placeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetPlaceForFlightDto {
    private int placeId;
    private String place;
    private String airPortName;

    public GetPlaceForFlightDto(int placeId, String place, String airPortName) {
        this.placeId = placeId;
        this.place = place;
        this.airPortName = airPortName;
    }
}
