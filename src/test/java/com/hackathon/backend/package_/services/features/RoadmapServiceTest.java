package com.hackathon.backend.package_.services.features;

import com.hackathon.backend.dto.packageDto.features.CreateRoadmapDto;
import com.hackathon.backend.dto.packageDto.features.EditRoadmapDto;
import com.hackathon.backend.dto.packageDto.features.GetRoadmapDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.repositories.package_.PackageDetailsRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.RoadmapRepository;
import com.hackathon.backend.services.package_.packageFeatures.RoadmapService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoadmapServiceTest {

    @Mock
    RoadmapRepository roadmapRepository;

    @Mock
    PackageDetailsRepository packageDetailsRepository;

    @InjectMocks
    RoadmapService roadmapService;

    @Test
    void createRoadmap_ShouldReturnSuccess_WhenRoadmapIsValid() {
        // given
        CreateRoadmapDto createRoadmapDto = new CreateRoadmapDto();
        createRoadmapDto.setRoadmap("New Roadmap");

        //behavior
        when(roadmapRepository.existsByRoadmap("New Roadmap")).thenReturn(false);

        // when
        ResponseEntity<String> response = roadmapService.createRoadmap(createRoadmapDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Roadmap created successfully: New Roadmap", response.getBody());
    }

    @Test
    void createRoadmap_ShouldReturnBadRequest_WhenRoadmapAlreadyExists() {
        // given
        CreateRoadmapDto createRoadmapDto = new CreateRoadmapDto();
        createRoadmapDto.setRoadmap("Existing Roadmap");

        //behavior
        when(roadmapRepository.existsByRoadmap("Existing Roadmap")).thenReturn(true);

        // when
        ResponseEntity<String> response = roadmapService.createRoadmap(createRoadmapDto);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Roadmap already exists", response.getBody());
    }


    @Test
    void getRoadmaps_ShouldReturnAllRoadmaps() {
        // given
        List<GetRoadmapDto> roadmaps = List.of(
                new GetRoadmapDto(1, "Roadmap 1"),
                new GetRoadmapDto(2, "Roadmap 2")
        );

        //behavior
        when(roadmapRepository.findAllRoadmaps()).thenReturn(roadmaps);

        // when
        ResponseEntity<List<GetRoadmapDto>> response = roadmapService.getRoadmaps();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roadmaps, response.getBody());
    }


    @Test
    void editRoadmap_ShouldReturnSuccess_WhenRoadmapIsValid() {
        // given
        int roadmapId = 1;
        EditRoadmapDto editRoadmapDto = new EditRoadmapDto();
        editRoadmapDto.setRoadmap("Updated Roadmap");

        RoadmapEntity roadmapEntity = new RoadmapEntity("Old Roadmap");

        // behavior
        when(roadmapRepository.findById(roadmapId)).thenReturn(Optional.of(roadmapEntity));

        // when
        ResponseEntity<String> response = roadmapService.editRoadmap(roadmapId, editRoadmapDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Roadmap edited successfully: Updated Roadmap", response.getBody());
        assertEquals("Updated Roadmap", roadmapEntity.getRoadmap());
        verify(roadmapRepository).save(roadmapEntity);
    }

    @Test
    void editRoadmap_ShouldReturnBadRequest_WhenRoadmapIsEmpty() {
        // given
        int roadmapId = 1;
        EditRoadmapDto editRoadmapDto = new EditRoadmapDto();

        // when
        ResponseEntity<String> response = roadmapService.editRoadmap(roadmapId, editRoadmapDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("You sent an empty data to change", response.getBody());
    }

    @Test
    void deleteRoadmap_ShouldReturnSuccess_WhenRoadmapExists() {
        // Arrange
        int roadmapId = 1;
        RoadmapEntity roadmapEntity = new RoadmapEntity("Roadmap to Delete");
        when(roadmapRepository.findById(roadmapId)).thenReturn(Optional.of(roadmapEntity));

        // Simulate packageDetails relationship
        PackageDetailsEntity packageDetails = new PackageDetailsEntity();
        packageDetails.getRoadmaps().add(roadmapEntity);
        roadmapEntity.setPackageDetails(List.of(packageDetails));

        // Act
        ResponseEntity<String> response = roadmapService.deleteRoadmap(roadmapId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Roadmap deleted successfully", response.getBody());
        verify(roadmapRepository).delete(roadmapEntity);
        verify(packageDetailsRepository).save(packageDetails);
    }
}