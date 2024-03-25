package com.hackathon.backend.Controllers;

import com.hackathon.backend.Dto.CountryDto.CountryDetailsDto;
import com.hackathon.backend.Dto.CountryDto.CountryDto;
import com.hackathon.backend.Services.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(path = "${COUNTRY_API_PATH}")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping(path = "${COUNTRY_CREATE_PATH}")
    public ResponseEntity<?> createCountry(@RequestBody CountryDto countryDto){
        return countryService.createCountry(countryDto);
    }

    @PostMapping(path = "${COUNTRY_CREATE_DETAILS_PATH}")
    public ResponseEntity<?> createCountryDetails(@PathVariable("countryID") int countryID, @RequestBody CountryDetailsDto countryDetailsDto){
        return countryService.createCountryDetails(countryID,countryDetailsDto);
    }

    @GetMapping(path = "${COUNTRY_GET_PATH}")
    public ResponseEntity<?> getAllCountries(){
        return countryService.getAllCountries();
    }

    @GetMapping(path = "${COUNTRY_GET_SINGLE_PATH}")
    public ResponseEntity<?> getSingleCountry(@PathVariable("countryID") int countryID){
        return countryService.getSingleCountry(countryID);
    }

    @GetMapping(path = "${COUNTRY_GET_DETAILS_PATH}")
    public ResponseEntity<?> getAllCountriesDetails(){
        return countryService.getAllCountriesDetails();
    }

    @GetMapping(path = "${COUNTRY_GET_SINGLE_DETAIL_PATH}")
    public ResponseEntity<?> getSingleCountryDetail(@PathVariable("countryDetailsID") int countryDetailsID){
        return countryService.getSingleCountryDetails(countryDetailsID);
    }

    @PutMapping(path = "${COUNTRY_EDIT_PATH}")
    public ResponseEntity<?> editCountry(@PathVariable("countryID") int countryID, @RequestBody CountryDto countryDto){
        return countryService.editCountry(countryID,countryDto);
    }

    @PutMapping(path = "${COUNTRY_EDIT_DETAILS_PATH}")
    public ResponseEntity<?> editCountryDetails(@PathVariable("countryDetailsID") int countryDetailsID, @RequestBody CountryDetailsDto countryDetailsDto){
        return countryService.editCountryDetails(countryDetailsID,countryDetailsDto);
    }

    @DeleteMapping(path = "${COUNTRY_DELETE_PATH}")
    public ResponseEntity<?> deleteCountry(@PathVariable("countryID") int countryID){
        return countryService.deleteCountry(countryID);
    }

    @DeleteMapping(path = "${COUNTRY_DELETE_DETAILS_PATH}")
    public ResponseEntity<?> deleteCountryDetails(@PathVariable("countryDetailsID") int countryCountryID){
        return countryService.deleteCountryDetails(countryCountryID);
    }
}
