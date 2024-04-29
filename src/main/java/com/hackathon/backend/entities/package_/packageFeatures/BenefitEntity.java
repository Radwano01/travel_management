package com.hackathon.backend.entities.package_.packageFeatures;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public BenefitEntity(String benefit){
        this.benefit = benefit;
    }
}