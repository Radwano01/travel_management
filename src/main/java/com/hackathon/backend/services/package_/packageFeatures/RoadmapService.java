package com.hackathon.backend.services.package_.packageFeatures;

import com.hackathon.backend.dto.packageDto.features.CreateRoadmapDto;
import com.hackathon.backend.dto.packageDto.features.EditRoadmapDto;
import com.hackathon.backend.dto.packageDto.features.GetRoadmapDto;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import java.util.List;

public interface RoadmapService {

    ResponseEntity<String> createRoadmap(@NonNull CreateRoadmapDto createRoadmapDto);

    ResponseEntity<List<GetRoadmapDto>> getRoadmaps();

    ResponseEntity<String> editRoadmap(int roadmapId, EditRoadmapDto editRoadmapDto);

    ResponseEntity<String> deleteRoadmap(int roadmapId);
}
