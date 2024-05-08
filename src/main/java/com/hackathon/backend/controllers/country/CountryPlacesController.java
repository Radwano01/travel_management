package com.hackathon.backend.controllers.country;

import com.hackathon.backend.dto.countryDto.placeDto.PlaceDto;
import com.hackathon.backend.services.country.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class CountryPlacesController {

    private final PlaceService placeService;
    @Autowired
    public CountryPlacesController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @PostMapping(path = "${CREATE_PLACE_PATH}")
    public ResponseEntity<?> createPlace(@PathVariable("countryId") int countryId,
                                         @RequestBody PlaceDto placeDto){
        return placeService.createPlace(countryId,placeDto);
    }

    @GetMapping(path = "${GET_PLACES_PATH}")
    public ResponseEntity<?> getPlaces(@PathVariable("countryId") int countryId){
        return placeService.getPlacesByCountry(countryId);
    }

    @PutMapping(path = "${EDIT_PLACE_PATH}")
    public ResponseEntity<?> editPlace(@PathVariable("countryId") int countryId,
                                       @PathVariable("placeId") int placeId,
                                       @RequestBody PlaceDto placeDto){
        return placeService.editPlace(countryId,placeId,placeDto);
    }

    @DeleteMapping(path = "${DELETE_PLACE_PATH}")
    public ResponseEntity<?> deletePlace(@PathVariable("placeId") int placeId){
        return placeService.deletePlace(placeId);
    }
}
