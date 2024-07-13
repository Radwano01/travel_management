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
    private Integer hotelRoomsCount;
    private String address;
    private Integer price;
    private Integer rate;

    public EditHotelDto(String hotelName,
                        MultipartFile mainImage,
                        String description,
                        Integer hotelRoomsCount,
                        String address,
                        Integer price,
                        Integer rate) {
        this.hotelName = hotelName;
        this.mainImage = mainImage;
        this.description = description;
        this.hotelRoomsCount = hotelRoomsCount;
        this.address = address;
        this.price = price;
        this.rate = rate;
    }
}
