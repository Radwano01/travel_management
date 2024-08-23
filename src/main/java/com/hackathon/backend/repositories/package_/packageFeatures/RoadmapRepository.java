package com.hackathon.backend.repositories.package_.packageFeatures;

import com.hackathon.backend.dto.packageDto.features.GetRoadmapDto;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoadmapRepository extends JpaRepository<RoadmapEntity, Integer> {
    boolean existsByRoadmap(String roadmap);

    @Query("SELECT new com.hackathon.backend.dto.packageDto.features.GetRoadmapDto" +
            "(r.id, r.roadmap) " +
            "FROM RoadmapEntity r")
    List<GetRoadmapDto> findAllRoadmaps();
}