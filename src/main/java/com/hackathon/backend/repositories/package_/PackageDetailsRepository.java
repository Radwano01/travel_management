package com.hackathon.backend.repositories.package_;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageDetailsRepository extends JpaRepository<PackageDetailsEntity, Integer> {
    void deleteByPackageOfferId(int packageId);

    Optional<PackageDetailsEntity> findByPackageOfferId(int packageId);
}
