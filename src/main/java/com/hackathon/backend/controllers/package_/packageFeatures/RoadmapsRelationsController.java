package com.hackathon.backend.controllers.package_.packageFeatures;

import com.hackathon.backend.services.package_.packageFeatures.PackageRoadmapsRelationsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoadmapsRelationsController {

    private final PackageRoadmapsRelationsService packageRoadmapsRelationsService;

    @Autowired
    public RoadmapsRelationsController(PackageRoadmapsRelationsService packageRoadmapsRelationsService){
        this.packageRoadmapsRelationsService = packageRoadmapsRelationsService;
    }

    @PostMapping(path = "${PACKAGE_ROADMAP_RELATIONS_PATH}")
    public ResponseEntity<String> addPackageRoadmap(@PathVariable("packageId") int packageId,
                                                    @PathVariable("roadmapId") int roadmapId){
        try {
            return packageRoadmapsRelationsService.addPackageRoadmap(packageId, roadmapId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${PACKAGE_ROADMAP_RELATIONS_PATH}")
    public ResponseEntity<String> removePackageRoadmap(@PathVariable("packageId") int packageId,
                                                  @PathVariable("roadmapId") int roadmapId){
        try {
            return packageRoadmapsRelationsService.removePackageRoadmap(packageId, roadmapId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
