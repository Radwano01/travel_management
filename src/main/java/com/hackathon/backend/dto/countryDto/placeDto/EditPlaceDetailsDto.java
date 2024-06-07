package com.hackathon.backend.dto.countryDto.placeDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EditPlaceDetailsDto {
    private MultipartFile imageOne;
    private MultipartFile imageTwo;
    private MultipartFile imageThree;
    private String description;

    public EditPlaceDetailsDto(MultipartFile imageOne,
                               MultipartFile imageTwo,
                               MultipartFile imageThree,
                               String description) {
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
    }
}
