package com.hackathon.backend.dto.hotelDto;


import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetHotelDto {

    private long id;
    private String hotelName;
    private String mainImage;
    private String description;
    private String address;
    private int rate;

    public GetHotelDto(long id, String hotelName,
                       String mainImage, String description,
                       String address, int rate) {
        this.id = id;
        this.hotelName = hotelName;
        this.mainImage = mainImage;
        this.description = description;
        this.address = address;
        this.rate = rate;
    }

}
