package com.hackathon.backend.dto.packageDto;

import com.hackathon.backend.dto.packageDto.features.GetBenefitDto;
import com.hackathon.backend.dto.packageDto.features.GetRoadmapDto;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetPackageANDPackageDetailsDto {

    private int id;
    private String packageName;
    private int price;
    private int rate;
    private String mainImage;

    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String description;

    private List<GetRoadmapDto> roadmaps;
    private List<GetBenefitDto> benefits;

    public GetPackageANDPackageDetailsDto(int id, String packageName, int price, int rate, String mainImage,
                                          String imageOne, String imageTwo, String imageThree,
                                          String description, List<GetRoadmapDto> roadmaps,
                                          List<GetBenefitDto> benefits) {
        this.id = id;
        this.packageName = packageName;
        this.price = price;
        this.rate = rate;
        this.mainImage = mainImage;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
        this.roadmaps = roadmaps;
        this.benefits = benefits;
    }
}
