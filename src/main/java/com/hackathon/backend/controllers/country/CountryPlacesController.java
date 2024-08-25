package com.hackathon.backend.controllers.country;

import com.hackathon.backend.dto.countryDto.placeDto.CreatePlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDto;
import com.hackathon.backend.services.country.impl.PlaceServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class CountryPlacesController {

    private final PlaceServiceImpl placeServiceImpl;
    @Autowired
    public CountryPlacesController(PlaceServiceImpl placeServiceImpl) {
        this.placeServiceImpl = placeServiceImpl;
    }

    @PostMapping(path = "${CREATE_PLACE_PATH}")
    public ResponseEntity<?> createPlace(@PathVariable("countryId") int countryId,
                                         @ModelAttribute CreatePlaceDto createPlaceDto){
        try{
            return placeServiceImpl.createPlace(countryId, createPlaceDto);
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_PLACES_PATH}")
    public ResponseEntity<?> getPlaces(@PathVariable("countryId") int countryId){
        try{
            return placeServiceImpl.getAllPlacesByCountryId(countryId);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_PLACE_FOR_FLIGHTS_PATH}")
    public ResponseEntity<?> getPlace(@RequestParam("place") String place){
        try {
            return placeServiceImpl.getPlaceByPlace(place);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_PLACE_PATH}")
    public ResponseEntity<?> editPlace(@PathVariable("countryId") int countryId,
                                       @PathVariable("placeId") int placeId,
                                       @ModelAttribute EditPlaceDto editPlaceDto){
        try {
            return placeServiceImpl.editPlace(countryId, placeId, editPlaceDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_PLACE_PATH}")
    public ResponseEntity<?> deletePlace(@PathVariable("countryId") int countryId,
                                         @PathVariable("placeId") int placeId){
        try {
            return placeServiceImpl.deletePlace(countryId, placeId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
