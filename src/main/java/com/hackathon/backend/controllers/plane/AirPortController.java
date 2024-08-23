package com.hackathon.backend.controllers.plane;

import com.hackathon.backend.dto.planeDto.AirPortDto;
import com.hackathon.backend.services.plane.AirPortService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

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
        try {
            return airPortService.createAirPort(placeId, airPortDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_AIRPORT_PATH}")
    public ResponseEntity<?> getAirports(@PathVariable("placeId") int placeId){
        try {
            return airPortService.getAirPortsByPlaceId(placeId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_AIRPORT_PATH}")
    public ResponseEntity<String> editAirPort(@PathVariable("placeId") int placeId,
                                              @PathVariable("airPortId") long airPortId,
                                              @RequestBody AirPortDto airPortDto){
        try {
            return airPortService.editAirPort(placeId, airPortId, airPortDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_AIRPORT_PATH}")
    public ResponseEntity<String> deleteAirPort(@PathVariable("placeId") int placeId,
                                                @PathVariable("airPortId") long airPortId){
        try {
            return airPortService.deleteAirPort(placeId, airPortId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}