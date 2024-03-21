package com.hackathon.backend.Repositories;

import com.hackathon.backend.Entities.BenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitRepository extends JpaRepository<BenefitEntity, Integer> {
    boolean existsByBenefit(String benefit);
}
