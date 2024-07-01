package com.hackathon.backend.controllers.plane;

import com.hackathon.backend.dto.planeDto.AirPortDto;
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
    public ResponseEntity<String> createAirPort(@PathVariable("placeId") int placeId,
                                                @RequestBody AirPortDto airPortDto){
        return airPortService.createAirPort(placeId, airPortDto);
    }

    @GetMapping(path = "${GET_AIRPORT_PATH}")
    public ResponseEntity<?> getAirports(@PathVariable("placeId") int placeId){
        return airPortService.getAirPortsByPlaceId(placeId);
    }

    @PutMapping(path = "${EDIT_AIRPORT_PATH}")
    public ResponseEntity<String> editAirPort(@PathVariable("airPortId") long airPortId,
                                            @RequestBody AirPortDto airPortDto){
        return airPortService.editAirPort(airPortId, airPortDto);
    }

    @DeleteMapping(path = "${DELETE_AIRPORT_PATH}")
    public ResponseEntity<String> deleteAirPort(@PathVariable("airPortId") long airPortId){
        return airPortService.deleteAirPort(airPortId);
    }
}