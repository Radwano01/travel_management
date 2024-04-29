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
public class RoomDetailsDto {
    private long id;
    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String imageFour;
    private String description;
    private int price;


    public RoomDetailsDto(String imageOne, String imageTwo,
                          String imageThree, String imageFour,
                          String description, int price) {
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.imageFour = imageFour;
        this.description = description;
        this.price = price;
    }
}
