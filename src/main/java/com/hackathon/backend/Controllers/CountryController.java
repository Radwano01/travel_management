package com.hackathon.backend.Controllers;


import com.hackathon.backend.Dto.CountryDto.CountryDto;
import com.hackathon.backend.Services.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@Controller
@RequestMapping(path = "/api/v1/country")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping(path = "/create-new-country")
    public ResponseEntity<?> createCountry(@RequestBody CountryDto countryDto){
        return countryService.createCountry(countryDto);
    }

    @GetMapping(path = "/get-all-countries")
    public ResponseEntity<?> getAllCountries(){
        return countryService.getAllCountries();
    }

    @PutMapping(path = "/edit-country/{id}")
    public ResponseEntity<?> editCountry(@PathVariable("id") int id, @RequestBody CountryDto countryDto){
        return countryService.editCountry(id,countryDto);
    }

    @DeleteMapping(path = "/delete-country/{id}")
    public ResponseEntity<?> deleteCountry(@PathVariable("id") int id){
        return countryService.deleteCountry(id);
    }

}
