package com.hackathon.backend.Controllers;


import com.hackathon.backend.Dto.CountryDto.CountryDto;
import com.hackathon.backend.Services.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "${country.controller.path}")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping(path = "${country.create.path}")
    public ResponseEntity<?> createCountry(@RequestBody CountryDto countryDto){
        return countryService.createCountry(countryDto);
    }

    @GetMapping(path = "${country.get.path}")
    public ResponseEntity<?> getAllCountries(){
        return countryService.getAllCountries();
    }

    @PutMapping(path = "${country.edit.path}")
    public ResponseEntity<?> editCountry(@PathVariable("id") int id, @RequestBody CountryDto countryDto){
        return countryService.editCountry(id,countryDto);
    }

    @DeleteMapping(path = "${country.delete.path}")
    public ResponseEntity<?> deleteCountry(@PathVariable("id") int id){
        return countryService.deleteCountry(id);
    }
}

