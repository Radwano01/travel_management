package com.hackathon.backend.package_.repositories.features;

import com.hackathon.backend.dto.packageDto.features.GetRoadmapDto;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.repositories.package_.packageFeatures.RoadmapRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoadmapRepositoryTest {

    @Autowired
    RoadmapRepository roadmapRepository;

    @BeforeEach
    void setUp() {
        // Create and save a roadmap
        RoadmapEntity roadmap = new RoadmapEntity();
        roadmap.setRoadmap("Roadmap to Success");
        roadmapRepository.save(roadmap);
    }

    @AfterEach
    void tearDown() {
        roadmapRepository.deleteAll();
    }

    @Test
    void itShouldReturnExistRoadmapByRoadmap() {
        // Given
        String roadmapName = "Roadmap to Success";

        // When
        boolean exists = roadmapRepository.existsByRoadmap(roadmapName);

        // Then
        assertTrue(exists, "Roadmap should exist in the repository");
    }

    @Test
    void itShouldReturnNotFoundRoadmapByRoadmap() {
        // Given
        String roadmapName = "Nonexistent Roadmap";

        // When
        boolean exists = roadmapRepository.existsByRoadmap(roadmapName);

        // Then
        assertFalse(exists, "Roadmap should not exist in the repository");
    }

    @Test
    void findAllRoadmaps() {
        // When
        List<GetRoadmapDto> roadmaps = roadmapRepository.findAllRoadmaps();

        // Then
        assertNotNull(roadmaps);
        assertFalse(roadmaps.isEmpty());
        GetRoadmapDto roadmapDto = roadmaps.get(0);
        assertEquals("Roadmap to Success", roadmapDto.getRoadmap());
    }
}
