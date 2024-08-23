package com.hackathon.backend.controllers.country;

import com.hackathon.backend.dto.countryDto.CreateCountryDto;
import com.hackathon.backend.dto.countryDto.EditCountryDto;
import com.hackathon.backend.services.country.CountryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class CountryController {
    private final CountryService countryService;
    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping(path = "${CREATE_COUNTRY_PATH}")
    private ResponseEntity<?> createCountry(@ModelAttribute CreateCountryDto createCountryDto){
        try{
            return countryService.createCountry(createCountryDto);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_COUNTRIES_PATH}")
    private ResponseEntity<?> getCountries(){
        return countryService.getAllCountries();
    }


    @PutMapping(path = "${EDIT_COUNTRY_PATH}")
    private ResponseEntity<?> editCountry(@PathVariable("countryId") int countryId,
                                          @ModelAttribute EditCountryDto editCountryDto){
        try{
            return countryService.editCountry(countryId, editCountryDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_COUNTRY_PATH}")
    private ResponseEntity<?> deleteCountry(@PathVariable("countryId") int countryId){
        try{
            return countryService.deleteCountry(countryId);
        }catch (Exception e){
            return serverErrorException(e);
        }

    }
}
