package com.hackathon.backend.entities.package_.packageFeatures;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roadmaps")
@Getter
@Setter
@NoArgsConstructor
public class RoadmapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String roadmap;

    @ManyToMany
    private List<PackageDetailsEntity> packageDetails = new ArrayList<>();

    public RoadmapEntity(String roadmap){
        this.roadmap = roadmap;
    }
}