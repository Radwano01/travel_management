package com.hackathon.backend.controllers.hotel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PostH {
    private String hotelName;
    private MultipartFile mainImage;
    private String description;
    private int hotelRoomsCount;
    private String address;
    private float rate;
    private MultipartFile imageOne;
    private MultipartFile imageTwo;
    private MultipartFile imageThree;
    private MultipartFile imageFour;
    private String roomDescription;
    private int price;

    public PostH(String hotelName, MultipartFile mainImage,
                 String description, int hotelRoomsCount,
                 String address, float rate,
                 MultipartFile imageOne, MultipartFile imageTwo,
                 MultipartFile imageThree, MultipartFile imageFour,
                 String roomDescription, int price) {
        this.hotelName = hotelName;
        this.mainImage = mainImage;
        this.description = description;
        this.hotelRoomsCount = hotelRoomsCount;
        this.address = address;
        this.rate = rate;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.imageFour = imageFour;
        this.roomDescription = roomDescription;
        this.price = price;
    }
}
