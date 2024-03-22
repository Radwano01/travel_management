package com.hackathon.backend.Controllers;

import com.hackathon.backend.Dto.PlaneDto.PlaneDto;
import com.hackathon.backend.Dto.PlaneDto.VisaDto;
import com.hackathon.backend.Services.PlaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "${PLANE_API_PATH}")
public class PlaneController {

    private final PlaneService planeService;

    @Autowired
    public PlaneController(PlaneService planeService){
        this.planeService = planeService;
    }

    @PostMapping(path = "${PLANE_CREATE_PLANE_PATH}")
    public ResponseEntity<?> createPlane(@RequestBody PlaneDto planeDto){
        return planeService.createPlane(planeDto);
    }

    @PostMapping(path = "${PLANE_CREATE_VISA_PATH}")
    public ResponseEntity<?> createVisa(@RequestBody VisaDto visaDto){
        return planeService.createVisa(visaDto);
    }

    @GetMapping(path = "${PLANE_GET_PLANES_PATH}")
    public ResponseEntity<?> getAllPlanes(){
        return planeService.getAllPlanes();
    }

    @GetMapping(path = "${PLANE_GET_VISAS_PATH}")
    public ResponseEntity<?> getAllVisasFromPlane(@RequestBody VisaDto visaDto){
        return planeService.getAllVisasFromPlane(visaDto);
    }

    @PutMapping(path="${PLANE_EDIT_PLANE_PATH}")
    public ResponseEntity<?> editPlane(@PathVariable int id,@RequestBody PlaneDto planeDto){
        return planeService.editPlane(id, planeDto);
    }

    @PutMapping(path = "${PLANE_EDIT_VISA_PATH}")
    public ResponseEntity<?> editVisa(@PathVariable int id,@RequestBody VisaDto visaDto){
        return planeService.editVisa(id,visaDto);
    }

    @DeleteMapping(path = "${PLANE_DELETE_PLANE_PATH}")
    public ResponseEntity<?> deletePlane(@PathVariable int id){
        return planeService.deletePlane(id);
    }

    @DeleteMapping(path = "${PLANE_DELETE_VISA_PATH}")
    public ResponseEntity<?> deleteVisa(@PathVariable int id){
        return planeService.deleteVisa(id);
    }

}
