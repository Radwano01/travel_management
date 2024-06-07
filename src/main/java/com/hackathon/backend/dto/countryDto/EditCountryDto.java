package com.hackathon.backend.dto.countryDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EditCountryDto {
    private String country;
    private MultipartFile mainImage;

    public EditCountryDto(String country,
                          MultipartFile mainImage) {
        this.country = country;
        this.mainImage = mainImage;
    }
}
