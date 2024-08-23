package com.hackathon.backend.services.package_.packageFeatures;

import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.RoadmapRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.alreadyValidException;
import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;

@Service
public class PackageRoadmapsRelationsService {

    private final PackageRepository packageRepository;
    private final RoadmapRepository roadmapRepository;

    @Autowired
    public PackageRoadmapsRelationsService(PackageRepository packageRepository,
                                           RoadmapRepository roadmapRepository) {
        this.packageRepository = packageRepository;
        this.roadmapRepository = roadmapRepository;
    }

    @Transactional
    public ResponseEntity<String> addPackageRoadmap(int packageId, int roadmapId) {
        PackageEntity packageEntity = getPackageById(packageId);

        RoadmapEntity roadmapEntity = findRoadmapById(roadmapId);

        if (checkIfRoadmapAlreadyExist(packageEntity, roadmapId) != null) {
            return alreadyValidException("This roadmap is already valid for this package");
        }

        packageEntity.getPackageDetails().getRoadmaps().add(roadmapEntity);

        packageRepository.save(packageEntity);

        return ResponseEntity.ok("Roadmap added successfully");
    }

    private PackageEntity getPackageById(int packageId){
        return packageRepository.findById(packageId)
                .orElseThrow(()-> new EntityNotFoundException("No such package has this id"));
    }

    private RoadmapEntity checkIfRoadmapAlreadyExist(PackageEntity packageEntity, int roadmapId) {
        Optional<RoadmapEntity> exist = packageEntity.getPackageDetails()
                .getRoadmaps().stream()
                .filter((roadmap) -> roadmap.getId() == roadmapId)
                .findFirst();
        return exist.orElse(null);
    }

    private RoadmapEntity findRoadmapById(int roadmapId) {
        return roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new EntityNotFoundException("No such roadmap has this id"));
    }

    @Transactional
    public ResponseEntity<String> removePackageRoadmap(int packageId, int roadmapId) {
        PackageEntity packageEntity = getPackageById(packageId);

        if (checkIfRoadmapAlreadyExist(packageEntity, roadmapId) == null) {
            notFoundException("This roadmap is not found in this package");
        }

        RoadmapEntity roadmapEntity = findRoadmapById(roadmapId);

        packageEntity.getPackageDetails().getRoadmaps().remove(roadmapEntity);

        packageRepository.save(packageEntity);

        return ResponseEntity.ok("Roadmap removed from this package");
    }
}
