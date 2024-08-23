package com.hackathon.backend.dto.packageDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class CreatePackageDto {
    private String packageName;
    private int price;
    private int rate;
    private MultipartFile mainImage;
    private MultipartFile imageOne;
    private MultipartFile imageTwo;
    private MultipartFile imageThree;
    private String description;

    public CreatePackageDto(String packageName, int price, int rate, MultipartFile mainImage,
                            MultipartFile imageOne, MultipartFile imageTwo,
                            MultipartFile imageThree, String description) {
        this.packageName = packageName;
        this.price = price;
        this.rate = rate;
        this.mainImage = mainImage;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
    }
}
