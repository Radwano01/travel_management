package com.hackathon.backend.Repositories;

import com.hackathon.backend.Entities.RoadmapEntity;
import com.hackathon.backend.Entities.TodoListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoadmapRepository extends JpaRepository<RoadmapEntity,Integer> {
    boolean existsByRoadmap(String roadmap);
}
