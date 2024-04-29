package com.hackathon.backend.utilities.package_.features;

import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.repositories.package_.packageFeatures.RoadmapRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoadmapUtils {

    private final RoadmapRepository roadmapRepository;

    @Autowired
    public RoadmapUtils(RoadmapRepository roadmapRepository) {
        this.roadmapRepository = roadmapRepository;
    }

    public boolean existsByRoadmap(String roadmap) {
        return roadmapRepository.existsByRoadmap(roadmap);
    }

    public void save(RoadmapEntity roadmapEntity) {
        roadmapRepository.save(roadmapEntity);
    }

    public List<RoadmapEntity> findAll() {
        return roadmapRepository.findAll();
    }

    public RoadmapEntity findById(int roadmapId) {
        return roadmapRepository.findById(roadmapId)
                .orElseThrow(()-> new EntityNotFoundException("Roadmap id not found"));
    }

    public void delete(RoadmapEntity roadmapEntity) {
        roadmapRepository.delete(roadmapEntity);
    }
}
