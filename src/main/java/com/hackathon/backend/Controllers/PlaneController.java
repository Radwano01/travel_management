package com.hackathon.backend.Controllers;

import com.hackathon.backend.Dto.PlaneDto.PlaneDto;
import com.hackathon.backend.Dto.PlaneDto.VisaDto;
import com.hackathon.backend.Dto.UserDto.AuthResponseDto;
import com.hackathon.backend.Dto.payment.PaymentDto;
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

    @PostMapping(path = "${PLANE_CREATE_PATH}")
    public ResponseEntity<?> createPlane(@RequestBody PlaneDto planeDto){
        return planeService.createPlane(planeDto);
    }


    @GetMapping(path = "${PLANE_GET_PATH}")
    public ResponseEntity<?> getAllPlanes(){
        return planeService.getAllPlanes();
    }

    @GetMapping(path = "${PLANE_GET_SINGLE_PATH}")
    public ResponseEntity<?> getSinglePlane(@PathVariable("planeID") int planeID){
        return planeService.getSinglePlane(planeID);
    }

    @PutMapping(path="${PLANE_EDIT_PATH}")
    public ResponseEntity<?> editPlane(@PathVariable("planeID") int planeID,@RequestBody PlaneDto planeDto){
        return planeService.editPlane(planeID, planeDto);
    }

    @DeleteMapping(path = "${PLANE_DELETE_PATH}")
    public ResponseEntity<?> deletePlane(@PathVariable("planeID") int planeID){
        return planeService.deletePlane(planeID);
    }

}
