package com.hackathon.backend.dto.packageDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PackageDetailsDto {

    private int id;
    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String description;

    public PackageDetailsDto(int id, String imageOne,
                             String imageTwo, String imageThree,
                             String description) {
        this.id = id;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
    }
}
