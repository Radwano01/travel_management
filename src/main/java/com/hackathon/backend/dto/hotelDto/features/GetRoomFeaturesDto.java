package com.hackathon.backend.dto.hotelDto.features;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetRoomFeaturesDto {
    private int id;
    private String roomFeature;

    public GetRoomFeaturesDto(int id, String roomFeature){
        this.id = id;
        this.roomFeature = roomFeature;
    }
}
