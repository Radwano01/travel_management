package com.hackathon.backend.Services;

import com.hackathon.backend.Dto.CountryDto.CountryDetailsDto;
import com.hackathon.backend.Dto.CountryDto.CountryDto;

import com.hackathon.backend.RelationShips.CountryDetailsEntity;
import com.hackathon.backend.Entities.CountryEntity;

import com.hackathon.backend.Repositories.CountryDetailsRepository;
import com.hackathon.backend.Repositories.CountryRepository;

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
    private final CountryDetailsRepository countryDetailsRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository,
                          CountryDetailsRepository countryDetailsRepository) {


        this.countryRepository = countryRepository;
        this.countryDetailsRepository = countryDetailsRepository;
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



    @Transactional
    public ResponseEntity<?> createCountryDetails(int countryID,CountryDetailsDto countryDetailsDto){
        try{
            CountryEntity countryEntity = countryRepository.findById(countryID)
                        .orElseThrow(()-> new EntityNotFoundException("Country Id is Not Found"));
            CountryDetailsEntity countryDetailsEntity = new CountryDetailsEntity();
            countryDetailsEntity.setPlace(countryDetailsDto.getPlace());
            countryDetailsEntity.setDescription(countryDetailsDto.getDescription());
            countryDetailsEntity.setImageOne(countryDetailsDto.getImageOne());
            countryDetailsEntity.setImageTwo(countryDetailsDto.getImageTwo());
            countryDetailsEntity.setImageThree(countryDetailsDto.getImageThree());
            countryDetailsRepository.save(countryDetailsEntity);

            countryEntity.getCountryDetails().add(countryDetailsEntity);
            return new ResponseEntity<>("Country Details added Successfully", HttpStatus.OK);
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

    public ResponseEntity<?> getAllCountriesDetails(){
        try {
            List<CountryDetailsEntity> countryDetailsEntities = countryDetailsRepository.findAll();
            return new ResponseEntity<>(countryDetailsEntities, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getSingleCountryDetails(int countryDetailsID) {
        try{
            CountryDetailsEntity countryEntity = countryDetailsRepository.findById(countryDetailsID)
                    .orElseThrow(()-> new EntityNotFoundException("Country Details is Not Found: "+countryDetailsID));
            return new ResponseEntity<>(countryEntity, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteCountry(int countryID){
        try{
            countryRepository.deleteById(countryID);
            boolean isDeleted = !countryRepository.existsById(countryID);
            if (isDeleted) {
                return new ResponseEntity<>("Country Deleted Successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Country Not Found", HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteCountryDetails(int countryDetailsID){
        try{
            countryDetailsRepository.deleteById(countryDetailsID);
            boolean isDeleted = !countryDetailsRepository.existsById(countryDetailsID);
            if (isDeleted) {
                return new ResponseEntity<>("Country Details Deleted Successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Country Details Not Found", HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> editCountry(int countryID, CountryDto countryDto){
        try {
            CountryEntity countryEntity = countryRepository.findById(countryID)
                    .orElseThrow(()-> new EntityNotFoundException("Country Id Not Found"));
            if(countryDto.getCountry() != null){
                countryEntity.setCountry(countryDto.getCountry());
            }
            countryRepository.save(countryEntity);
            return new ResponseEntity<>("Country updated Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> editCountryDetails(int countryDetailsID, CountryDetailsDto countryDetailsDto){
        try {
            CountryDetailsEntity countryDetailsEntity = countryDetailsRepository.findById(countryDetailsID)
                    .orElseThrow(()-> new EntityNotFoundException("Country Details Id Not Found"));
            if(countryDetailsDto.getPlace() != null){
                countryDetailsEntity.setPlace(countryDetailsDto.getPlace());
            }
            if(countryDetailsDto.getImageOne() != null){
                countryDetailsEntity.setImageOne(countryDetailsDto.getImageOne());
            }
            if(countryDetailsDto.getImageTwo() != null){
                countryDetailsEntity.setImageTwo(countryDetailsDto.getImageTwo());
            }
            if(countryDetailsDto.getImageThree() != null){
                countryDetailsEntity.setImageThree(countryDetailsDto.getImageThree());
            }
            if(countryDetailsDto.getDescription() != null){
                countryDetailsEntity.setDescription(countryDetailsDto.getDescription());
            }
            return new ResponseEntity<>("Country updated Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
