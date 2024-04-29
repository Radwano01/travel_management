package com.hackathon.backend.repositories.package_.packageFeatures;

import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitRepository extends JpaRepository<BenefitEntity, Integer> {
    boolean existsByBenefit(String benefit);
}
