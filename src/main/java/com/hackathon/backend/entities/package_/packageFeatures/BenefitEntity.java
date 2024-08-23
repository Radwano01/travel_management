package com.hackathon.backend.entities.package_.packageFeatures;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "benefits")
public class BenefitEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String benefit;

    @ManyToMany(mappedBy = "benefits", fetch = FetchType.LAZY)
    private List<PackageDetailsEntity> packageDetails = new ArrayList<>();

    public BenefitEntity(String benefit){
        this.benefit = benefit;
    }
}