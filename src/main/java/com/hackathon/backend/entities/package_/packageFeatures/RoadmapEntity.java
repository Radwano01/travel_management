package com.hackathon.backend.entities.package_.packageFeatures;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
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
@Table(name = "roadmaps")
public class RoadmapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String roadmap;

    @ManyToMany(mappedBy = "roadmaps", fetch = FetchType.LAZY)
    private List<PackageDetailsEntity> packageDetails = new ArrayList<>();

    public RoadmapEntity(String roadmap){
        this.roadmap = roadmap;
    }
}