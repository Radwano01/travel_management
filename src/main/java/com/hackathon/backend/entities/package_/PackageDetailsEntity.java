package com.hackathon.backend.entities.package_;

import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "packageDetails")
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
                                PackageEntity packageEntity){
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
        this.packageOffer = packageEntity;
    }

    @Override
    public String toString() {
        return "PackageDetailsEntity{" +
                "id=" + id +
                ", imageOne='" + imageOne + '\'' +
                ", imageTwo='" + imageTwo + '\'' +
                ", imageThree='" + imageThree + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
