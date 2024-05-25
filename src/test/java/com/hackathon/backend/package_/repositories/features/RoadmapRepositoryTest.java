package com.hackathon.backend.package_.repositories.features;

import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.repositories.package_.packageFeatures.RoadmapRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoadmapRepositoryTest {

    @Autowired
    RoadmapRepository roadmapRepository;

    @Test
    void existsByRoadmap() {
        //given
        RoadmapEntity roadmap = new RoadmapEntity(
                "testFeature"
        );
        roadmapRepository.save(roadmap);

        //when
        boolean response = roadmapRepository.existsByRoadmap("testFeature");

        //then
        assertTrue(response);

        roadmapRepository.deleteAll();
    }
}