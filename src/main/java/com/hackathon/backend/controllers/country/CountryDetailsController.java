package com.hackathon.backend.controllers.country;


import com.hackathon.backend.dto.countryDto.CountryDetailsDto;
import com.hackathon.backend.services.country.CountryDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class CountryDetailsController {

    private final CountryDetailsService countryDetailsService;
    @Autowired
    public CountryDetailsController(CountryDetailsService countryDetailsService) {
        this.countryDetailsService = countryDetailsService;
    }

    //create post

    @GetMapping(path = "${GET_SINGLE_COUNTRY_DETAIL_PATH}")
    public ResponseEntity<?> getSingleCountryDetails(@PathVariable("countryId") int countryId){
        return countryDetailsService.getSingleCountryDetails(countryId);
    }

    @PutMapping(path = "${EDIT_COUNTRY_DETAILS_PATH}")
    public ResponseEntity<?> editCountryDetails(@PathVariable("countryId") int countryId,
                                                @RequestBody CountryDetailsDto countryDetailsDto){
        return countryDetailsService.editCountryDetails(countryId,countryDetailsDto);
    }
}
