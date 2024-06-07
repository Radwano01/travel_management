package com.hackathon.backend.controllers.country;

import com.hackathon.backend.dto.countryDto.EditCountryDto;
import com.hackathon.backend.dto.countryDto.PostCountryDto;
import com.hackathon.backend.services.country.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "${BASE_API}")
public class CountryController {
    private final CountryService countryService;
    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping(path = "${CREATE_COUNTRY_PATH}")
    public ResponseEntity<?> createCountry(@RequestParam("country") String countryName,
                                           @RequestParam("mainImage") MultipartFile mainImage,
                                           @RequestParam("imageOne") MultipartFile imageOne,
                                           @RequestParam("imageTwo") MultipartFile imageTwo,
                                           @RequestParam("imageThree") MultipartFile imageThree,
                                           @RequestParam("description") String description){
        PostCountryDto postCountryDto = new PostCountryDto(
                countryName,
                mainImage,
                imageOne,
                imageTwo,
                imageThree,
                description
        );

        return countryService.createCountry(postCountryDto);
    }

    @GetMapping(path = "${GET_COUNTRIES_PATH}")
    public ResponseEntity<?> getCountries(){
        return countryService.getCountry();
    }

    @PutMapping(path = "${EDIT_COUNTRY_PATH}")
    public ResponseEntity<?> editCountry(@PathVariable("countryId") int countryId,
                                         @RequestParam(name = "country", required = false) String country,
                                         @RequestParam(name = "mainImage", required = false) MultipartFile mainImage){
        EditCountryDto editCountryDto = new EditCountryDto(
                country,
                mainImage
        );
        return countryService.editCountry(countryId, editCountryDto);
    }

    @DeleteMapping(path = "${DELETE_COUNTRY_PATH}")
    public ResponseEntity<?> deleteCountry(@PathVariable("countryId") int countryId){
        return countryService.deleteCountry(countryId);
    }
}
