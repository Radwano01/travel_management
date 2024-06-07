package com.hackathon.backend.dto.packageDto;

import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetPackageDto {

    private int id;
    private String packageName;
    private float price;
    private float rate;
    private String mainImage;

    private GetPackageDetailsDto packageDetails;
    private List<RoadmapEntity> roadmaps;
    private List<BenefitEntity> benefits;

    public GetPackageDto(int id, String packageName,
                         float price, float rate,
                         String mainImage,
                         GetPackageDetailsDto getPackageDetailsDto,
                         List<RoadmapEntity> roadmaps,
                         List<BenefitEntity> benefits) {
        this.id = id;
        this.packageName = packageName;
        this.price = price;
        this.rate = rate;
        this.mainImage = mainImage;
        this.packageDetails = getPackageDetailsDto;
        this.roadmaps = roadmaps;
        this.benefits = benefits;
    }
}
