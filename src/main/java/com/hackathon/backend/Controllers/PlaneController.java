package com.hackathon.backend.Controllers;


import com.hackathon.backend.Dto.PlaneDto.PlaneDto;
import com.hackathon.backend.Dto.PlaneDto.VisaDto;
import com.hackathon.backend.Services.PlaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/v1/plane")
public class PlaneController {

    private final PlaneService planeService;
    @Autowired
    public PlaneController(PlaneService planeService){
        this.planeService = planeService;
    }

    @PostMapping(path = "/create-new-plane")
    public ResponseEntity<?> createPlane(@RequestBody PlaneDto planeDto){
        return planeService.createPlane(planeDto);
    }

    @PostMapping(path = "/create-new-visa")
    public ResponseEntity<?> createVisa(@RequestBody VisaDto visaDto){
        return planeService.createVisa(visaDto);
    }

    @GetMapping(path = "/get-all-planes")
    public ResponseEntity<?> getAllPlanes(){
        return planeService.getAllPlanes();
    }

    @GetMapping(path = "/get-all-visas")
    public ResponseEntity<?> getAllVisasFromPlane(@RequestBody VisaDto visaDto){
        return planeService.getAllVisasFromPlane(visaDto);
    }

    @PutMapping(path="/edit-plane/{id}")
    public ResponseEntity<?> editPlane(@PathVariable int id,@RequestBody PlaneDto planeDto){
        return planeService.editPlane(id, planeDto);
    }

    @PutMapping(path = "/edit-visa/{id}")
    public ResponseEntity<?> editVisa(@PathVariable int id,@RequestBody VisaDto visaDto){
        return planeService.editVisa(id,visaDto);
    }

    @DeleteMapping(path = "/delete-plane/{id}")
    public ResponseEntity<?> deletePlane(@PathVariable int id){
        return planeService.deletePlane(id);
    }

    @DeleteMapping(path = "/delete-visa/{id}")
    public ResponseEntity<?> deleteVisa(@PathVariable int id){
        return planeService.deleteVisa(id);
    }

}
