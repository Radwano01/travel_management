package com.hackathon.backend.Controllers;

import com.hackathon.backend.Dto.CountryDto.CountryDto;
import com.hackathon.backend.Services.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
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

    @GetMapping(path = "${COUNTRY_GET_PATH}")
    public ResponseEntity<?> getAllCountries(){
        return countryService.getAllCountries();
    }

    @PutMapping(path = "${COUNTRY_EDIT_PATH}")
    public ResponseEntity<?> editCountry(@PathVariable("id") int id, @RequestBody CountryDto countryDto){
        return countryService.editCountry(id,countryDto);
    }

    @DeleteMapping(path = "${COUNTRY_DELETE_PATH}")
    public ResponseEntity<?> deleteCountry(@PathVariable("id") int id){
        return countryService.deleteCountry(id);
    }
}
