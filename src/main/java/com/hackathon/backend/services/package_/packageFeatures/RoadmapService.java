package com.hackathon.backend.services.package_.packageFeatures;

import com.hackathon.backend.dto.packageDto.features.CreateRoadmapDto;
import com.hackathon.backend.dto.packageDto.features.EditRoadmapDto;
import com.hackathon.backend.dto.packageDto.features.GetRoadmapDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.repositories.package_.PackageDetailsRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.RoadmapRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final PackageDetailsRepository packageDetailsRepository;

    @Autowired
    public RoadmapService(RoadmapRepository roadmapRepository,
                          PackageDetailsRepository packageDetailsRepository) {
        this.roadmapRepository = roadmapRepository;
        this.packageDetailsRepository = packageDetailsRepository;
    }

    public ResponseEntity<String> createRoadmap(@NonNull CreateRoadmapDto createRoadmapDto) {
        String roadmap = createRoadmapDto.getRoadmap().trim();

        ResponseEntity<String> checkResult = checkIfRoadmapAlreadyExists(roadmap);
        if (!checkResult.getStatusCode().equals(HttpStatus.OK)) {
            return checkResult;
        }

        roadmapRepository.save(new RoadmapEntity(roadmap));

        return ResponseEntity.ok("Roadmap created successfully: " + roadmap);
    }

    private ResponseEntity<String> checkIfRoadmapAlreadyExists(String roadmap) {
        boolean existsRoadmap = roadmapRepository.existsByRoadmap(roadmap);

        if (existsRoadmap) {
            return alreadyValidException("Roadmap already exists");
        }
        return ResponseEntity.ok("OK");
    }

    public ResponseEntity<List<GetRoadmapDto>> getRoadmaps() {
        return ResponseEntity.ok(roadmapRepository.findAllRoadmaps());
    }

    @Transactional
    public ResponseEntity<String> editRoadmap(int roadmapId, EditRoadmapDto editRoadmapDto) {
        if (!checkIfSentEmptyData(editRoadmapDto)) {
            return badRequestException("You sent an empty data to change");
        }

        String roadmap = editRoadmapDto.getRoadmap().trim();

        RoadmapEntity roadmapEntity = findRoadmapById(roadmapId);

        roadmapEntity.setRoadmap(roadmap);

        roadmapRepository.save(roadmapEntity);

        return ResponseEntity.ok("Roadmap edited successfully: " + roadmap);
    }

    private RoadmapEntity findRoadmapById(int roadmapId) {
        return roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new EntityNotFoundException("No such roadmap has this id"));
    }

    @Transactional
    public ResponseEntity<String> deleteRoadmap(int roadmapId) {
        RoadmapEntity roadmapEntity = findRoadmapById(roadmapId);

        for (PackageDetailsEntity packageDetails : roadmapEntity.getPackageDetails()) {
            packageDetails.getRoadmaps().remove(roadmapEntity);
            packageDetailsRepository.save(packageDetails);
        }

        roadmapRepository.delete(roadmapEntity);

        return ResponseEntity.ok("Roadmap deleted successfully");
    }
}
