package com.hackathon.backend.package_.services.features;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.services.package_.packageFeatures.PackageRoadmapsRelationsService;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.hackathon.backend.utilities.package_.features.RoadmapUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackageRoadmapsRelationsServiceTest {

    @Mock
    private PackageUtils packageUtils;

    @Mock
    private RoadmapUtils roadmapUtils;

    private PackageRoadmapsRelationsService packageRoadmapsRelationsService;

    @BeforeEach
    void setUp() {
        packageRoadmapsRelationsService = new PackageRoadmapsRelationsService(
                packageUtils,
                roadmapUtils
        );
    }

    @Test
    void addPackageRoadmap() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageDetails(new PackageDetailsEntity());
        RoadmapEntity roadmapEntity = new RoadmapEntity();

        when(packageUtils.findById(1)).thenReturn(packageEntity);
        when(roadmapUtils.findById(1)).thenReturn(roadmapEntity);

        packageEntity.getPackageDetails().setRoadmaps(new ArrayList<>());

        //when
        ResponseEntity<?> response = packageRoadmapsRelationsService.addPackageRoadmap(1, 1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void removePackageRoadmap() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageDetails(new PackageDetailsEntity());
        RoadmapEntity roadmapEntity = new RoadmapEntity();
        roadmapEntity.setId(1);

        when(packageUtils.findById(1)).thenReturn(packageEntity);
        when(roadmapUtils.findById(1)).thenReturn(roadmapEntity);
        packageEntity.getPackageDetails().getRoadmaps().add(roadmapEntity);

        //when
        ResponseEntity<?> response = packageRoadmapsRelationsService.removePackageRoadmap(1, 1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}