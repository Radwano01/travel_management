package com.hackathon.backend.controllers.country;


import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDetailsDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceDetailsDto;
import com.hackathon.backend.services.country.PlaceDetailsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

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
        try {
            return placeDetailsService.getSinglePlaceDetails(placeId);
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_PLACE_DETAILS_PATH}")
    public ResponseEntity<?> editPlaceDetails(@PathVariable("placeId") int placeId,
                                              @ModelAttribute EditPlaceDetailsDto editPlaceDetailsDto) {
        try {
            return placeDetailsService.editPlaceDetails(placeId, editPlaceDetailsDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
