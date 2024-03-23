package com.hackathon.backend.Services;

import com.hackathon.backend.Dto.CountryDto.CountryDto;

import com.hackathon.backend.Entities.CountryEntity;

import com.hackathon.backend.Repositories.CountryRepository;

import com.hackathon.backend.Repositories.TodoListRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {


        this.countryRepository = countryRepository;
    }

    public ResponseEntity<?> createCountry(CountryDto countryDto) {
        try{
            boolean countryEntity = countryRepository.existsByCountry(countryDto.getCountry());
            if(!countryEntity) {
                CountryEntity country = new CountryEntity();
                country.setCountry(countryDto.getCountry());
                countryRepository.save(country);
                return new ResponseEntity<>("Country added Successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Country Already Valid", HttpStatus.CONFLICT);
            }

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getAllCountries() {
        try {
            List<CountryEntity> countryEntities = countryRepository.findAll();
            return new ResponseEntity<>(countryEntities, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> getSingleCountry(int countryID) {
        try{
            CountryEntity countryEntity = countryRepository.findById(countryID)
                    .orElseThrow(()-> new EntityNotFoundException("Country is Not Found: "+countryID));
            return new ResponseEntity<>(countryEntity, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteCountry(int countryID){
        try{
            countryRepository.deleteById(countryID);
            return new ResponseEntity<>("Country Deleted Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> editCountry(int countryID, CountryDto countryDto){
        try {
            CountryEntity countryEntity = countryRepository.findById(countryID)
                    .orElseThrow(()-> new EntityNotFoundException("Country Id Not Found"));
            countryEntity.setCountry(countryDto.getCountry());
            countryRepository.save(countryEntity);
            return new ResponseEntity<>("Country updated Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
