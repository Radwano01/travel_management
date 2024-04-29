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
public class PackageDto {

    private int id;
    private String packageName;
    private float price;
    private float rate;
    private String mainImage;

    private PackageDetailsDto packageDetails;
    private List<RoadmapEntity> roadmaps;
    private List<BenefitEntity> benefits;

    public PackageDto(int id, String packageName,
                      float price, float rate,
                      String mainImage,
                      PackageDetailsDto packageDetailsDto,
                      List<RoadmapEntity> roadmaps,
                      List<BenefitEntity> benefits) {
        this.id = id;
        this.packageName = packageName;
        this.price = price;
        this.rate = rate;
        this.mainImage = mainImage;
        this.packageDetails = packageDetailsDto;
        this.roadmaps = roadmaps;
        this.benefits = benefits;
    }
}
