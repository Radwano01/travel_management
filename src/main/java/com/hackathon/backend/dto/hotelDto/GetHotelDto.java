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
    private int hotelRoomsCount;
    private String address;
    private float rate;
    private GetRoomDetailsDto roomDetails;
    private List<HotelFeaturesEntity> hotelFeatures;
    private List<RoomFeaturesEntity> roomFeatures;

    public GetHotelDto(long id, String hotelName,
                       String mainImage, String description,
                       String address, float rate) {
        this.id = id;
        this.hotelName = hotelName;
        this.mainImage = mainImage;
        this.description = description;
        this.address = address;
        this.rate = rate;
    }

    public GetHotelDto(long id, String hotelName,
                       String address, float rate,
                       GetRoomDetailsDto getRoomDetailsDto,
                       List<HotelFeaturesEntity> hotelFeatures,
                       List<RoomFeaturesEntity> roomFeatures) {
        this.id = id;
        this.hotelName = hotelName;
        this.address = address;
        this.rate = rate;
        this.roomDetails = getRoomDetailsDto;
        this.hotelFeatures = hotelFeatures;
        this.roomFeatures = roomFeatures;
    }

}
