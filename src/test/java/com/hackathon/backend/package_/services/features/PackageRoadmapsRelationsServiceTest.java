package com.hackathon.backend.package_.services.features;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.RoadmapRepository;
import com.hackathon.backend.services.package_.packageFeatures.PackageRoadmapsRelationsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackageRoadmapsRelationsServiceTest {

    @Mock
    PackageRepository packageRepository;

    @Mock
    RoadmapRepository roadmapRepository;

    @InjectMocks
    PackageRoadmapsRelationsService packageRoadmapsRelationsService;

    @Test
    void addPackageRoadmap_ShouldAddRoadmapSuccessfully() {
        // given
        int packageId = 1;
        int roadmapId = 1;

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageDetails(new PackageDetailsEntity());
        RoadmapEntity roadmapEntity = new RoadmapEntity("Roadmap 1");

        //behavior
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));
        when(roadmapRepository.findById(roadmapId)).thenReturn(Optional.of(roadmapEntity));

        // when
        ResponseEntity<String> response = packageRoadmapsRelationsService.addPackageRoadmap(packageId, roadmapId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Roadmap added successfully", response.getBody());
        assertTrue(packageEntity.getPackageDetails().getRoadmaps().contains(roadmapEntity));
        verify(packageRepository).save(packageEntity);
    }

    @Test
    void addPackageRoadmap_ShouldReturnAlreadyValidException_WhenRoadmapAlreadyExists() {
        // given
        int packageId = 1;
        int roadmapId = 1;

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageDetails(new PackageDetailsEntity());
        RoadmapEntity roadmapEntity = new RoadmapEntity("Roadmap 1");
        roadmapEntity.setId(roadmapId);
        packageEntity.getPackageDetails().getRoadmaps().add(roadmapEntity);

        //behavior
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));
        when(roadmapRepository.findById(roadmapId)).thenReturn(Optional.of(roadmapEntity));

        // when
        ResponseEntity<String> response = packageRoadmapsRelationsService.addPackageRoadmap(packageId, roadmapId);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("This roadmap is already valid for this package", response.getBody());
    }


    @Test
    void removePackageRoadmap_ShouldRemoveRoadmapSuccessfully() {
        // given
        int packageId = 1;
        int roadmapId = 1;

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageDetails(new PackageDetailsEntity());
        RoadmapEntity roadmapEntity = new RoadmapEntity("Roadmap 1");
        roadmapEntity.setId(roadmapId);
        packageEntity.getPackageDetails().getRoadmaps().add(roadmapEntity);

        //behavior
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));
        when(roadmapRepository.findById(roadmapId)).thenReturn(Optional.of(roadmapEntity));

        // when
        ResponseEntity<String> response = packageRoadmapsRelationsService.removePackageRoadmap(packageId, roadmapId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Roadmap removed from this package", response.getBody());
        assertFalse(packageEntity.getPackageDetails().getRoadmaps().contains(roadmapEntity));
        verify(packageRepository).save(packageEntity);
    }

}