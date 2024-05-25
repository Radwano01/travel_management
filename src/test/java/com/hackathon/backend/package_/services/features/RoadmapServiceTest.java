package com.hackathon.backend.package_.services.features;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.services.package_.packageFeatures.RoadmapService;
import com.hackathon.backend.utilities.package_.features.RoadmapUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RoadmapServiceTest {

    @Mock
    RoadmapUtils roadmapUtils;

    @Mock
    PackageDetailsEntity packageDetails;

    @InjectMocks
    RoadmapService roadmapService;

    @Test
    void createRoadmap() {
        //given
        String roadmap = "testRoadmap";

        //behavior
        when(roadmapUtils.existsByRoadmap(roadmap)).thenReturn(false);

        //when
        ResponseEntity<?> response = roadmapService.createRoadmap(roadmap);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getRoadmaps() {
        //given
        RoadmapEntity roadmap = new RoadmapEntity("testRoadmap");

        List<RoadmapEntity> roadmapEntities = new ArrayList<>();
        roadmapEntities.add(roadmap);

        //behavior
        when(roadmapUtils.findAll()).thenReturn(roadmapEntities);

        //when
        ResponseEntity<?> response = roadmapService.getRoadmaps();

        List<RoadmapEntity> responseData = (List<RoadmapEntity>) response.getBody();
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roadmapEntities, responseData);
    }

    @Test
    void editRoadmap() {
        //given
        int roadmapId = 1;
        String roadmap = "testRoadmap1";

        RoadmapEntity roadmapEntity = new RoadmapEntity("testRoadmap");

        //behavior
        when(roadmapUtils.findById(roadmapId)).thenReturn(roadmapEntity);

        //when
        ResponseEntity<?> response = roadmapService.editRoadmap(roadmapId, roadmap);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roadmap, roadmapEntity.getRoadmap());
    }

    @Test
    void deleteRoadmap() {
        //given
        int roadmapId = 1;

        RoadmapEntity roadmapEntity = new RoadmapEntity();
        roadmapEntity.setId(roadmapId);
        roadmapEntity.setRoadmap("testRoadmap");

        PackageDetailsEntity packageDetails = new PackageDetailsEntity();
        packageDetails.setRoadmaps(List.of(roadmapEntity));

        //behavior
        when(roadmapUtils.findById(roadmapId)).thenReturn(roadmapEntity);

        //when
        ResponseEntity<?> response = roadmapService.deleteRoadmap(roadmapId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roadmapUtils).delete(roadmapEntity);
    }
}