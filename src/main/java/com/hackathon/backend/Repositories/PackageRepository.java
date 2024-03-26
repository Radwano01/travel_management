package com.hackathon.backend.Repositories;

import com.hackathon.backend.Entities.CountryEntity;
import com.hackathon.backend.Entities.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Integer> {
    boolean existsByPackageName(String packageName);

    List<PackageEntity> findByCountryId(int countryID);

    List<PackageEntity> findAllByCountryId(int countryID);
}
