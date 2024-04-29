package com.hackathon.backend.dto.countryDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountryDto {
    private int id;
    private String country;
    private String mainImage;

    public CountryDto(Integer id,
                      String country,
                      String mainImage) {
        this.id = id;
        this.country = country;
        this.mainImage = mainImage;
    }
}
