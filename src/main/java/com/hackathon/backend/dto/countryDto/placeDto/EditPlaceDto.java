package com.hackathon.backend.dto.countryDto.placeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EditPlaceDto {
    private String place;
    private MultipartFile mainImage;

    public EditPlaceDto(String place,
                        MultipartFile mainImage) {
        this.place = place;
        this.mainImage = mainImage;
    }
}
