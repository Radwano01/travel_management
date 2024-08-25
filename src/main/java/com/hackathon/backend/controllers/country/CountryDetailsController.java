package com.hackathon.backend.controllers.country;


import com.hackathon.backend.dto.countryDto.EditCountryDetailsDto;
import com.hackathon.backend.services.country.impl.CountryDetailsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class CountryDetailsController {

    private final CountryDetailsServiceImpl countryDetailsServiceImpl;
    @Autowired
    public CountryDetailsController(CountryDetailsServiceImpl countryDetailsServiceImpl) {
        this.countryDetailsServiceImpl = countryDetailsServiceImpl;
    }

    //create post

    @GetMapping(path = "${GET_SINGLE_COUNTRY_DETAIL_PATH}")
    public ResponseEntity<?> getSingleCountryDetails(@PathVariable("countryId") int countryId){
        try{
            return countryDetailsServiceImpl.getSingleCountryDetails(countryId);
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }

    }

    @PutMapping(path = "${EDIT_COUNTRY_DETAILS_PATH}")
    public ResponseEntity<?> editCountryDetails(@PathVariable("countryId") int countryId,
                                                @ModelAttribute EditCountryDetailsDto editCountryDetailsDto){
        try{
            return countryDetailsServiceImpl.editCountryDetails(countryId, editCountryDetailsDto);
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        }catch (Exception e) {
            return serverErrorException(e);
        }

    }
}
