package com.hackathon.backend.controllers.plane;

import com.hackathon.backend.dto.planeDto.PlaneAirPortDto;
import com.hackathon.backend.services.plane.AirPortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class AirPortController {

    private final AirPortService airPortService;

    @Autowired
    public AirPortController(AirPortService airPortService) {
        this.airPortService = airPortService;
    }

    @PostMapping(path = "${CREATE_AIRPORT_PATH}")
    public ResponseEntity<?> createAirPort(@PathVariable("placeId") int placeId,
                                           @RequestBody PlaneAirPortDto planeAirPortDto){
        return airPortService.createAirPort(placeId, planeAirPortDto);
    }

    @PutMapping(path = "${EDIT_AIRPORT_PATH}")
    public ResponseEntity<?> editAirPort(@PathVariable("airPortId") long airPortId,
                                         @RequestBody PlaneAirPortDto planeAirPortDto){
        return airPortService.editAirPort(airPortId, planeAirPortDto);
    }

    @DeleteMapping(path = "${DELETE_AIRPORT_PATH}")
    public ResponseEntity<?> deleteAirPort(@PathVariable("airPortId") long airPortId){
        return airPortService.deleteAirPort(airPortId);
    }
}