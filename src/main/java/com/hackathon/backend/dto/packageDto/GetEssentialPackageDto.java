package com.hackathon.backend.dto.packageDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetEssentialPackageDto {
    private int id;
    private String packageName;
    private String mainImage;

    public GetEssentialPackageDto(int id,
                                  String packageName,
                                  String mainImage){
        this.id = id;
        this.packageName = packageName;
        this.mainImage = mainImage;
    }
}
