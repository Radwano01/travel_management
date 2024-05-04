package com.hackathon.backend.entities.package_;

import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "packageDetails")
@Getter
@Setter
@NoArgsConstructor
public class PackageDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String imageOne;
    private String imageTwo;
    private String imageThree;

    @Column(length = 2000)
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id")
    private PackageEntity packageOffer;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "package_roadmap_relation",
            joinColumns = @JoinColumn(name = "package_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roadmap_id"))
    private List<RoadmapEntity> roadmaps = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "package_benefit_relation",
            joinColumns = @JoinColumn(name = "package_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "benefit_id"))
    private List<BenefitEntity> benefits = new ArrayList<>();

    public PackageDetailsEntity(String imageOne,
                                String imageTwo,
                                String imageThree,
                                String description,
                                PackageEntity packageOffer){
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
        this.packageOffer = packageOffer;
    }
}
