package com.hackathon.backend.dto.countryDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class PostCountryDto {
    private String country;
    private MultipartFile mainImage;
    private MultipartFile imageOne;
    private MultipartFile imageTwo;
    private MultipartFile imageThree;
    private String description;

    public PostCountryDto(String countryName, MultipartFile mainImage,
                          MultipartFile imageOne, MultipartFile imageTwo,
                          MultipartFile imageThree, String description) {
        this.country = countryName;
        this.mainImage = mainImage;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
    }
}
