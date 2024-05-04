package com.hackathon.backend.package_.repositories;

import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.repositories.package_.packageFeatures.RoadmapRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoadmapRepositoryTest {

    @Autowired
    private RoadmapRepository roadmapRepository;

    @Test
    void testExistsByRoadmap() {
        //given
        RoadmapEntity roadmap = new RoadmapEntity();
        roadmap.setRoadmap("Attraction Locations");
        roadmapRepository.save(roadmap);

        //when
        boolean exists = roadmapRepository.existsByRoadmap("Attraction Locations");

        //then
        assertTrue(exists);
    }
}