package com.hackathon.backend.dto.hotelDto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetRoomDetailsDto {
    private long id;
    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String imageFour;
    private String description;
    private int price;


    public GetRoomDetailsDto(String imageOne, String imageTwo,
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
