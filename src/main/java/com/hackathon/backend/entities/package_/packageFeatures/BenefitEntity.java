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
@Table(name = "benefits")
@Getter
@Setter
@NoArgsConstructor
public class BenefitEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String benefit;

    @ManyToMany
    private List<PackageDetailsEntity> packageDetails = new ArrayList<>();

    public BenefitEntity(String benefit){
        this.benefit = benefit;
    }
}