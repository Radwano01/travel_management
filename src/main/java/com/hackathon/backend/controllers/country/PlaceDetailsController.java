package com.hackathon.backend.controllers.country;


import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDetailsDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceDetailsDto;
import com.hackathon.backend.services.country.PlaceDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<String> editPlaceDetails(@PathVariable("placeId") int placeId,
                                              @RequestParam(name = "imageOne", required = false) MultipartFile imageOne,
                                              @RequestParam(name = "imageTwo", required = false) MultipartFile imageTwo,
                                              @RequestParam(name = "imageThree", required = false) MultipartFile imageThree,
                                              @RequestParam(name = "description", required = false) String description) {
        EditPlaceDetailsDto editPlaceDetailsDto = new EditPlaceDetailsDto(
                imageOne,
                imageTwo,
                imageThree,
                description
        );
        return placeDetailsService.editPlaceDetails(placeId, editPlaceDetailsDto);
    }
}
