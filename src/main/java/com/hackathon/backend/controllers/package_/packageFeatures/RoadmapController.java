package com.hackathon.backend.controllers.package_.packageFeatures;

import com.hackathon.backend.dto.packageDto.features.CreateRoadmapDto;
import com.hackathon.backend.dto.packageDto.features.EditRoadmapDto;
import com.hackathon.backend.services.package_.packageFeatures.RoadmapService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class RoadmapController{

    private final RoadmapService roadmapService;

    @Autowired
    public RoadmapController(RoadmapService roadmapService){
        this.roadmapService = roadmapService;
    }

    @PostMapping(path = "${CREATE_ROADMAP_PATH}")
    public ResponseEntity<String> createRoadmap(@RequestBody CreateRoadmapDto createRoadmapDto){
        try {
            return roadmapService.createRoadmap(createRoadmapDto);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_ROADMAPS_PATH}")
    public ResponseEntity<?> getRoadmaps(){
        try {
            return roadmapService.getRoadmaps();
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_ROADMAP_PATH}")
    public ResponseEntity<String> editRoadmap(@PathVariable("roadmapId") int roadmapId,
                                              @RequestBody EditRoadmapDto editRoadmapDto){
        try {
            return roadmapService.editRoadmap(roadmapId, editRoadmapDto);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_ROADMAP_PATH}")
    public ResponseEntity<String> deleteRoadmap(@PathVariable("roadmapId") int roadmapId){
        try {
            return roadmapService.deleteRoadmap(roadmapId);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }
}