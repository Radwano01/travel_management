package com.hackathon.backend.repositories.package_;

import com.hackathon.backend.dto.packageDto.GetEssentialPackageDto;
import com.hackathon.backend.entities.package_.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Integer> {

    @Query("SELECT new com.hackathon.backend.dto.packageDto.GetEssentialPackageDto" +
            "(p.id, p.packageName, p.mainImage, p.price)" +
            " FROM PackageEntity p WHERE p.country.id = :countryId")
    List<GetEssentialPackageDto> findPackagesByCountryId(int countryId);
}
