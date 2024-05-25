package com.hackathon.backend.repositories.package_;

import com.hackathon.backend.entities.package_.PackageBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageBookingRepository extends JpaRepository<PackageBookingEntity, Long> {
}
