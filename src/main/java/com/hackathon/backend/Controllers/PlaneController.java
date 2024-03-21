package com.hackathon.backend.Controllers;


import com.hackathon.backend.Dto.PlaneDto.PlaneDto;
import com.hackathon.backend.Dto.PlaneDto.VisaDto;
import com.hackathon.backend.Services.PlaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "${plane.controller.path}")
public class PlaneController {

    private final PlaneService planeService;

    @Autowired
    public PlaneController(PlaneService planeService){
        this.planeService = planeService;
    }

    @PostMapping(path = "${plane.create.plane.path}")
    public ResponseEntity<?> createPlane(@RequestBody PlaneDto planeDto){
        return planeService.createPlane(planeDto);
    }

    @PostMapping(path = "${plane.create.visa.path}")
    public ResponseEntity<?> createVisa(@RequestBody VisaDto visaDto){
        return planeService.createVisa(visaDto);
    }

    @GetMapping(path = "${plane.get.planes.path}")
    public ResponseEntity<?> getAllPlanes(){
        return planeService.getAllPlanes();
    }

    @GetMapping(path = "${plane.get.visas.path}")
    public ResponseEntity<?> getAllVisasFromPlane(@RequestBody VisaDto visaDto){
        return planeService.getAllVisasFromPlane(visaDto);
    }

    @PutMapping(path="${plane.edit.plane.path}")
    public ResponseEntity<?> editPlane(@PathVariable int id,@RequestBody PlaneDto planeDto){
        return planeService.editPlane(id, planeDto);
    }

    @PutMapping(path = "${plane.edit.visa.path}")
    public ResponseEntity<?> editVisa(@PathVariable int id,@RequestBody VisaDto visaDto){
        return planeService.editVisa(id,visaDto);
    }

    @DeleteMapping(path = "${plane.delete.plane.path}")
    public ResponseEntity<?> deletePlane(@PathVariable int id){
        return planeService.deletePlane(id);
    }

    @DeleteMapping(path = "${plane.delete.visa.path}")
    public ResponseEntity<?> deleteVisa(@PathVariable int id){
        return planeService.deleteVisa(id);
    }

}

