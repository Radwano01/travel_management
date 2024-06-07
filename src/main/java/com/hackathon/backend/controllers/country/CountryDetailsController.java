package com.hackathon.backend.controllers.country;


import com.hackathon.backend.dto.countryDto.EditCountryDetailsDto;
import com.hackathon.backend.dto.countryDto.GetCountryDetailsDto;
import com.hackathon.backend.services.country.CountryDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                                                @RequestParam(name = "imageOne", required = false) MultipartFile imageOne,
                                                @RequestParam(name = "imageTwo", required = false) MultipartFile imageTwo,
                                                @RequestParam(name = "imageThree", required = false) MultipartFile imageThree,
                                                @RequestParam(name = "description",required = false) String description){
        EditCountryDetailsDto editCountryDetailsDto = new EditCountryDetailsDto(
                imageOne,
                imageTwo,
                imageThree,
                description
        );
        return countryDetailsService.editCountryDetails(countryId, editCountryDetailsDto);
    }
}
