package com.hackathon.backend.repositories.package_.packageFeatures;

import com.hackathon.backend.dto.packageDto.features.GetBenefitDto;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitRepository extends JpaRepository<BenefitEntity, Integer> {

    boolean existsBenefitByBenefit(String benefit);

    @Query("SELECT new com.hackathon.backend.dto.packageDto.features.GetBenefitDto" +
            "(b.id, b.benefit) FROM BenefitEntity b")
    List<GetBenefitDto> findAllBenefits();
}
