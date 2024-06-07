package com.hackathon.backend.dto.hotelDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EditRoomDetailsDto {
    private MultipartFile imageOne;
    private MultipartFile imageTwo;
    private MultipartFile imageThree;
    private MultipartFile imageFour;
    private String description;
    private int price;

    public EditRoomDetailsDto(MultipartFile imageOne,
                              MultipartFile imageTwo,
                              MultipartFile imageThree,
                              MultipartFile imageFour,
                              String description,
                              Integer price) {
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.imageFour = imageFour;
        this.description = description;
        this.price = price;
    }
}
