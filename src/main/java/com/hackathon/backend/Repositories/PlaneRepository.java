package com.hackathon.backend.Repositories;


import com.hackathon.backend.Entities.PlaneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaneRepository extends JpaRepository<PlaneEntity, Integer> {

    boolean existsByPlaneName(String planeName);

    Optional<PlaneEntity> findByPlaneName(String planeName);
}
