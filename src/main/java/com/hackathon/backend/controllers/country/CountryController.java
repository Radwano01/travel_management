package com.hackathon.backend.controllers.country;

import com.hackathon.backend.dto.countryDto.CountryDto;
import com.hackathon.backend.dto.countryDto.CountryWithDetailsDto;
import com.hackathon.backend.services.country.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(path = "${BASE_API}")
public class CountryController {
    private final CountryService countryService;
    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping(path = "${CREATE_COUNTRY_PATH}")
    public ResponseEntity<?> createCountry(@RequestBody CountryWithDetailsDto countryWithDetailsDto){
        return countryService.createCountry(countryWithDetailsDto);
    }

    @GetMapping(path = "${GET_COUNTRIES_PATH}")
    public ResponseEntity<?> getCountries(){
        return countryService.getCountries();
    }

    @PutMapping(path = "${EDIT_COUNTRY_PATH}")
    public ResponseEntity<?> editCountry(@PathVariable("countryId") int countryId,
                                         @RequestBody CountryDto countryDto){
        return countryService.editCountry(countryId,countryDto);
    }

    @DeleteMapping(path = "${DELETE_COUNTRY_PATH}")
    public ResponseEntity<?> deleteCountry(@PathVariable("countryId") int countryId){
        return countryService.deleteCountry(countryId);
    }
}
