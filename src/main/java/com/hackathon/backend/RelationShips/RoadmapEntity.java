package com.hackathon.backend.RelationShips;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="roadmaps")
public class RoadmapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String roadmap;


    public RoadmapEntity(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



//    public List<CountryEntity> getCountryEntity() {
//        return countryEntity;
//    }
//
//    public void setCountryEntity(List<CountryEntity> countryEntity) {
//        this.countryEntity = countryEntity;
//    }

    public String getRoadmap() {
        return roadmap;
    }

    public void setRoadmap(String roadmap) {
        this.roadmap = roadmap;
    }
}
