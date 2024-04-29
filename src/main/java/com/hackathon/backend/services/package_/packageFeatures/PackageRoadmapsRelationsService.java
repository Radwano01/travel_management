package com.hackathon.backend.services.package_.packageFeatures;

import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.RoadmapRepository;
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

    private final PackageRepository packageRepository;
    private final RoadmapRepository roadmapRepository;

    @Autowired
    public PackageRoadmapsRelationsService(PackageRepository packageRepository,
                                           RoadmapRepository roadmapRepository) {
        this.packageRepository = packageRepository;
        this.roadmapRepository = roadmapRepository;
    }

    @Transactional
    public ResponseEntity<?> addPackageRoadmap(int packageId, int roadmapId) {
        try{
            PackageEntity packageEntity = packageRepository.findById(packageId)
                    .orElseThrow(()-> new EntityNotFoundException("Package id not found"));
            RoadmapEntity roadmapEntity = roadmapRepository.findById(roadmapId)
                    .orElseThrow(()-> new EntityNotFoundException("roadmap id not found"));
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
            Optional<PackageEntity> packageEntity = packageRepository.findById(packageId);
            Optional<RoadmapEntity> roadmapEntity = roadmapRepository.findById(roadmapId);
            if(packageEntity.isPresent() && roadmapEntity.isPresent()) {
                Optional<RoadmapEntity> roadmapEntityOptional = packageEntity.get().getPackageDetails().getRoadmaps().stream()
                        .filter((roadmap) -> roadmap.getId() == roadmapId)
                        .findFirst();
                if(roadmapEntityOptional.isPresent()) {
                    packageEntity.get().getPackageDetails().getRoadmaps().remove(roadmapEntityOptional.get());
                    packageRepository.save(packageEntity.get());
                    roadmapRepository.save(roadmapEntity.get());
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
