package com.hackathon.backend.package_.services.features;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.services.package_.packageFeatures.PackageRoadmapsRelationsService;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.hackathon.backend.utilities.package_.features.RoadmapUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackageRoadmapsRelationsServiceTest {

    @Mock
    PackageUtils packageUtils;

    @Mock
    RoadmapUtils roadmapUtils;

    @InjectMocks
    PackageRoadmapsRelationsService packageRoadmapsRelationsService;

    @Test
    void addPackageRoadmap() {
        //given
        int packageId = 1;
        int roadmapId = 1;

        RoadmapEntity roadmapEntity = new RoadmapEntity();
        roadmapEntity.setId(roadmapId);
        roadmapEntity.setRoadmap("testRoadmap");

        PackageDetailsEntity packageDetails = new PackageDetailsEntity();

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);
        packageEntity.setPackageDetails(packageDetails);

        //behavior
        when(roadmapUtils.findById(roadmapId)).thenReturn(roadmapEntity);
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);

        //when
        ResponseEntity<?> response = packageRoadmapsRelationsService.addPackageRoadmap(packageId,roadmapId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roadmapUtils).findById(roadmapId);
        verify(packageUtils).findById(packageId);
        verify(roadmapUtils).save(roadmapEntity);
        verify(packageUtils).save(packageEntity);
    }

    @Test
    void removePackageRoadmap() {
        //given
        int packageId = 1;
        int roadmapId = 1;

        RoadmapEntity roadmapEntity = new RoadmapEntity();
        roadmapEntity.setId(roadmapId);
        roadmapEntity.setRoadmap("testRoadmap");

        PackageDetailsEntity packageDetails = new PackageDetailsEntity();
        packageDetails.getRoadmaps().add(roadmapEntity);

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);
        packageEntity.setPackageDetails(packageDetails);

        //behavior
        when(roadmapUtils.findById(roadmapId)).thenReturn(roadmapEntity);
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);

        //when
        ResponseEntity<?> response = packageRoadmapsRelationsService.removePackageRoadmap(packageId,roadmapId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roadmapUtils).findById(roadmapId);
        verify(packageUtils).findById(packageId);
        verify(roadmapUtils).save(roadmapEntity);
        verify(packageUtils).save(packageEntity);
    }
}