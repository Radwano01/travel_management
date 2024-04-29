package com.hackathon.backend.entities.package_;

import com.hackathon.backend.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "packageEvaluation")
@Getter
@Setter
@NoArgsConstructor
public class PackageEvaluationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String comment;
    private float rate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id")
    private PackageEntity packageEntity;

    public PackageEvaluationEntity(String comment, float rate,
                                   UserEntity user,
                                   PackageEntity packageEntity){
        this.comment = comment;
        this.rate = rate;
        this.user = user;
        this.packageEntity = packageEntity;
    }
}
