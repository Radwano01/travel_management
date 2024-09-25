package com.hackathon.backend.dto.hotelDto;


import com.hackathon.backend.dto.hotelDto.features.GetHotelFeaturesDto;
import com.hackathon.backend.dto.hotelDto.features.GetRoomFeaturesDto;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetRoomDetailsDto {
    private long id;
    private String hotelName;
    private String address;
    private float rate;
    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String imageFour;
    private String description;
    private int price;
    List<GetHotelFeaturesDto> hotelFeatures;
    List<GetRoomFeaturesDto> roomFeatures;

    public GetRoomDetailsDto(long id, String hotelName,
                             String address, float rate,
                             List<GetHotelFeaturesDto> hotelFeatures,
                             List<GetRoomFeaturesDto> roomFeatures,
                             String imageOne, String imageTwo,
                             String imageThree, String imageFour,
                             String description, int price) {
        this.id = id;
        this.hotelName = hotelName;
        this.address = address;
        this.rate = rate;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.imageFour = imageFour;
        this.description = description;
        this.price = price;
        this.hotelFeatures = hotelFeatures;
        this.roomFeatures = roomFeatures;
    }
}
