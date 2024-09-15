package com.hackathon.backend.repositories.package_;

import com.hackathon.backend.dto.packageDto.features.GetBenefitDto;
import com.hackathon.backend.dto.packageDto.features.GetRoadmapDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageDetailsRepository extends JpaRepository<PackageDetailsEntity, Integer> {

    @Query("SELECT new com.hackathon.backend.dto.packageDto.features.GetRoadmapDto(r.id, r.roadmap)" +
            " FROM PackageDetailsEntity pd JOIN pd.roadmaps r WHERE pd.packageOffer.id = :packageId")
    List<GetRoadmapDto> findPackageDetailsRoadmapsByPackageId(int packageId);

    @Query("SELECT new com.hackathon.backend.dto.packageDto.features.GetBenefitDto(b.id, b.benefit)" +
            " FROM PackageDetailsEntity pd JOIN pd.benefits b WHERE pd.packageOffer.id = :packageId")
    List<GetBenefitDto> findPackageDetailsBenefitsByPackageId(int packageId);
}
