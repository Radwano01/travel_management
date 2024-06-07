package com.hackathon.backend.dto.hotelDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EditHotelDto {
    private String hotelName;
    private MultipartFile mainImage;
    private String description;
    private int hotelRoomsCount;
    private String address;
    private float rate;

    public EditHotelDto(String hotelName,
                        MultipartFile mainImage,
                        String description,
                        int hotelRoomsCount,
                        String address,
                        float rate) {
        this.hotelName = hotelName;
        this.mainImage = mainImage;
        this.description = description;
        this.hotelRoomsCount = hotelRoomsCount;
        this.address = address;
        this.rate = rate;
    }
}
