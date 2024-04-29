package com.hackathon.backend.controllers.plane;

import com.hackathon.backend.dto.planeDto.PlaneDto;
import com.hackathon.backend.services.plane.PlaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PlaneController {

    private final PlaneService planeService;

    @Autowired
    public PlaneController(PlaneService planeService){
        this.planeService = planeService;
    }

    @PostMapping(path="${CREATE_PLANE_PATH}")
    public ResponseEntity<?> createPlane(@RequestBody PlaneDto planeDto){
        return planeService.createPlane(planeDto);
    }

    @PutMapping(path="${EDIT_PLANE_PATH}")
    public ResponseEntity<?> editPlane(@PathVariable("planeId") Long planeId,
                                       @RequestBody PlaneDto planeDto){
        return planeService.editPlane(planeId, planeDto);
    }

    @DeleteMapping(path = "${DELETE_PLANE_PATH}")
    public ResponseEntity<?> deletePlane(@PathVariable("planeId") Long planeId){
        return planeService.deletePlane(planeId);
    }
}
