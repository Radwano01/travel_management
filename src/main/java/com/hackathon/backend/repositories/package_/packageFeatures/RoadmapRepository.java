package com.hackathon.backend.repositories.package_.packageFeatures;

import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadmapRepository extends JpaRepository<RoadmapEntity, Integer> {
    boolean existsByRoadmap(String roadmap);
}