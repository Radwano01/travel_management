package com.hackathon.backend.controllers.package_.packageFeatures;

import com.hackathon.backend.services.package_.packageFeatures.PackageRoadmapsRelationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return packageRoadmapsRelationsService.addPackageRoadmap(packageId,roadmapId);
    }

    @DeleteMapping(path = "${PACKAGE_ROADMAP_RELATIONS_PATH}")
    public ResponseEntity<String> removePackageRoadmap(@PathVariable("packageId") int packageId,
                                                  @PathVariable("roadmapId") int roadmapId){
        return packageRoadmapsRelationsService.removePackageRoadmap(packageId,roadmapId);
    }
}
