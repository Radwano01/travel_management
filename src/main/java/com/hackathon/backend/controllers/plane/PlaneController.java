package com.hackathon.backend.controllers.plane;

import com.hackathon.backend.dto.planeDto.CreatePlaneDto;
import com.hackathon.backend.dto.planeDto.EditPlaneDto;
import com.hackathon.backend.services.plane.PlaneService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PlaneController {

    private final PlaneService planeService;

    @Autowired
    public PlaneController(PlaneService planeService){
        this.planeService = planeService;
    }

    @PostMapping(path="${CREATE_PLANE_PATH}")
    public ResponseEntity<String> createPlane(@RequestBody CreatePlaneDto createPlaneDto){
        try {
            return planeService.createPlane(createPlaneDto);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_PLANES_PATH}")
    public ResponseEntity<?> getPlanes(){
        try {
            return planeService.getPlanes();
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path="${EDIT_PLANE_PATH}")
    public ResponseEntity<String> editPlane(@PathVariable("planeId") Long planeId,
                                       @RequestBody EditPlaneDto editPlaneDto){
        try {
            return planeService.editPlane(planeId, editPlaneDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_PLANE_PATH}")
    public ResponseEntity<String> deletePlane(@PathVariable("planeId") Long planeId){
        try {
            return planeService.deletePlane(planeId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }
}
