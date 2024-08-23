package com.hackathon.backend.repositories.package_;

import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageEvaluationRepository extends JpaRepository<PackageEvaluationEntity, Long> {
    PackageEvaluationEntity findPackageEvaluationByUserId(long userId);
}
