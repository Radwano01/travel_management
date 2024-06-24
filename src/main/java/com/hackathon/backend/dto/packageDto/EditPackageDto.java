package com.hackathon.backend.dto.packageDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EditPackageDto {
    private String packageName;
    private float price;
    private int rate;
    private MultipartFile mainImage;

    public EditPackageDto(String packageName,
                          float price,
                          int rate,
                          MultipartFile mainImage) {
        this.packageName = packageName;
        this.price = price;
        this.rate = rate;
        this.mainImage = mainImage;
    }
}
