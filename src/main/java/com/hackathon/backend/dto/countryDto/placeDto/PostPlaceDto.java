package com.hackathon.backend.dto.countryDto.placeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PostPlaceDto {
    private String place;
    private MultipartFile mainImage;
    private MultipartFile imageOne;
    private MultipartFile imageTwo;
    private MultipartFile imageThree;
    private String description;

    public PostPlaceDto(String place, MultipartFile mainImage,
                        MultipartFile imageOne, MultipartFile imageTwo,
                        MultipartFile imageThree, String description) {
        this.place = place;
        this.mainImage = mainImage;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
    }

}
