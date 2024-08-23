package com.hackathon.backend.repositories.package_;

import com.hackathon.backend.dto.packageDto.GetPackageANDPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.PackageEvaluationDto;
import com.hackathon.backend.entities.package_.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Integer> {

    @Query("SELECT new com.hackathon.backend.dto.packageDto.PackageEvaluationDto" +
            "(pe.id, pe.comment, pe.rate, u.id, u.username, u.image)" +
            " FROM PackageEntity p JOIN p.packageEvaluations pe JOIN pe.user u WHERE p.id = :packageId")
    List<PackageEvaluationDto> findAllEvaluationsPackageByPackageId(int packageId);
}
