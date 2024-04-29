package com.hackathon.backend.controllers.package_.packageFeatures;

import com.hackathon.backend.services.package_.packageFeatures.RoadmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoadmapController{

    private final RoadmapService roadmapService;

    @Autowired
    public RoadmapController(RoadmapService roadmapService){
        this.roadmapService = roadmapService;
    }

    @PostMapping(path = "${CREATE_ROADMAP_PATH}")
    public ResponseEntity<?> createRoadmap(String roadmap){
        return roadmapService.createRoadmap(roadmap);
    }

    @GetMapping(path = "${GET_ROADMAPS_PATH}")
    public ResponseEntity<?> getRoadmaps(){
        return roadmapService.getRoadmaps();
    }

    @PutMapping(path = "${EDIT_ROADMAP_PATH}")
    public ResponseEntity<?> editRoadmap(@PathVariable("roadmapId") int roadmapId,
                                         String roadmap){
        return roadmapService.editRoadmap(roadmapId, roadmap);
    }

    @DeleteMapping(path = "${DELETE_ROADMAP_PATH}")
    public ResponseEntity<?> deleteRoadmap(@PathVariable("roadmapId") int roadmapId){
        return roadmapService.deleteRoadmap(roadmapId);
    }
}