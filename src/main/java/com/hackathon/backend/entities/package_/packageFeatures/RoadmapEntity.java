package com.hackathon.backend.entities.package_.packageFeatures;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public RoadmapEntity(String roadmap){
        this.roadmap = roadmap;
    }
}