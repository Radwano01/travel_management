package com.hackathon.backend.Repositories;

import com.hackathon.backend.RelationShips.RoadmapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadmapRepository extends JpaRepository<RoadmapEntity,Integer> {
    boolean existsByRoadmap(String roadmap);
}
