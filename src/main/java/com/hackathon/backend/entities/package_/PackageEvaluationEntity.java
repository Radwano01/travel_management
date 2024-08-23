package com.hackathon.backend.entities.package_;

import com.hackathon.backend.entities.user.UserEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "packageEvaluation")
public class PackageEvaluationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String comment;
    private int rate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id")
    private PackageEntity packageEntity;

    public PackageEvaluationEntity(String comment, int rate,
                                   UserEntity user,
                                   PackageEntity packageEntity){
        this.comment = comment;
        this.rate = rate;
        this.user = user;
        this.packageEntity = packageEntity;
    }

    @Override
    public String toString() {
        return "PackageEvaluationEntity{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", rate=" + rate +
                '}';
    }
}
