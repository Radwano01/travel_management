package com.hackathon.backend.repositories.package_;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageDetailsRepository extends JpaRepository<PackageDetailsEntity, Integer> {
}
