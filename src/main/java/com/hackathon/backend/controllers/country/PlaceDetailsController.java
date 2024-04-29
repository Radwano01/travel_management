package com.hackathon.backend.controllers.country;


import com.hackathon.backend.dto.countryDto.PlaceDto.PlaceDetailsDto;
import com.hackathon.backend.services.country.PlaceDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PlaceDetailsController {

    private final PlaceDetailsService placeDetailsService;
    @Autowired
    public PlaceDetailsController(PlaceDetailsService placeDetailsService) {
        this.placeDetailsService = placeDetailsService;
    }

    @GetMapping(path = "${GET_SINGLE_PLACE_PATH}")
    public ResponseEntity<?> getSinglePlace(@PathVariable("placeId") int placeId){
        return placeDetailsService.getSinglePlaceDetails(placeId);
    }

    @PutMapping(path = "${EDIT_PLACE_DETAILS_PATH}")
    public ResponseEntity<?> editPlaceDetails(@PathVariable("placeDetailsId") int placeDetailsId,
                                              @RequestBody PlaceDetailsDto placeDetailsDto) {
        return placeDetailsService.editPlaceDetails(placeDetailsId, placeDetailsDto);
    }
}
