package com.hackathon.backend.services.package_.packageFeatures;

import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.RoadmapRepository;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.hackathon.backend.utilities.package_.features.RoadmapUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PackageRoadmapsRelationsService {

    private final PackageUtils packageUtils;
    private final RoadmapUtils roadmapUtils;

    @Autowired
    public PackageRoadmapsRelationsService(PackageUtils packageUtils,
                                           RoadmapUtils roadmapUtils) {
        this.packageUtils = packageUtils;
        this.roadmapUtils = roadmapUtils;
    }

    @Transactional
    public ResponseEntity<?> addPackageRoadmap(int packageId, int roadmapId) {
        try{
            PackageEntity packageEntity = packageUtils.findById(packageId);
            RoadmapEntity roadmapEntity = roadmapUtils.findById(roadmapId);
            Optional<RoadmapEntity> roadmapEntityOptional = packageEntity.getPackageDetails()
                    .getRoadmaps().stream()
                    .filter((roadmap)-> roadmap.getId() == roadmapId)
                    .findFirst();
            if(roadmapEntityOptional.isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("This roadmap is already valid for this package");
            }
            packageEntity.getPackageDetails().getRoadmaps().add(roadmapEntity);
            return ResponseEntity.ok("Roadmap added successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> removePackageRoadmap(int packageId, int roadmapId) {
        try{
            PackageEntity packageEntity = packageUtils.findById(packageId);
            RoadmapEntity roadmapEntity = roadmapUtils.findById(roadmapId);
            if(packageEntity != null && roadmapEntity != null) {
                Optional<RoadmapEntity> roadmapEntityOptional = packageEntity.getPackageDetails().getRoadmaps().stream()
                        .filter((roadmap) -> roadmap.getId() == roadmapId)
                        .findFirst();
                if(roadmapEntityOptional.isPresent()) {
                    packageEntity.getPackageDetails().getRoadmaps().remove(roadmapEntityOptional.get());
                    packageUtils.save(packageEntity);
                    roadmapUtils.save(roadmapEntity);
                    return ResponseEntity.ok("Roadmap removed from this package");
                }else{
                    return notFoundException("Roadmap not found in this package");
                }
            }else{
                return notFoundException("Package or roadmap not found");
            }
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
