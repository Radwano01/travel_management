package com.hackathon.backend.package_.services.features;

import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.services.package_.packageFeatures.RoadmapService;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.features.RoadmapUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoadmapServiceTest {

    @Mock
    private RoadmapUtils roadmapUtils;
    @Mock
    private PackageDetailsUtils packageDetailsUtils;

    private RoadmapService roadmapService;

    @BeforeEach
    void setUp() {
        roadmapService = new RoadmapService(
                roadmapUtils,
                packageDetailsUtils
        );
    }

    @Test
    void createRoadmap() {
        //given
        when(roadmapUtils.existsByRoadmap("New Roadmap")).thenReturn(false);

        //when
        ResponseEntity<?> response = roadmapService.createRoadmap("New Roadmap");

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getRoadmaps() {
        //given
        List<RoadmapEntity> roadmaps = new ArrayList<>();
        roadmaps.add(new RoadmapEntity("Roadmap 1"));
        roadmaps.add(new RoadmapEntity("Roadmap 2"));
        when(roadmapUtils.findAll()).thenReturn(roadmaps);

        //when
        ResponseEntity<?> response = roadmapService.getRoadmaps();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editRoadmap() {
        //given
        RoadmapEntity roadmapEntity = new RoadmapEntity();
        when(roadmapUtils.findById(1)).thenReturn(roadmapEntity);

        //when
        ResponseEntity<?> response = roadmapService.editRoadmap(1, "Updated Roadmap");

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteRoadmap() {
        //given
        RoadmapEntity roadmapEntity = new RoadmapEntity();

        when(roadmapUtils.findById(1)).thenReturn(roadmapEntity);
        when(packageDetailsUtils.findAll()).thenReturn(new ArrayList<>());
        //when
        ResponseEntity<?> response = roadmapService.deleteRoadmap(1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}