package com.hackathon.backend.services.country.impl;

import com.hackathon.backend.dto.countryDto.EditCountryDetailsDto;
import com.hackathon.backend.dto.countryDto.GetCountryWithCountryDetailsDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.services.country.CountryDetailsService;
import com.hackathon.backend.utilities.S3Service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.hackathon.backend.utilities.ErrorUtils.*;
import static com.hackathon.backend.libs.MyLib.*;

@Service
public class CountryDetailsServiceImpl implements CountryDetailsService {

    private final CountryRepository countryRepository;
    private final S3Service s3Service;

    @Autowired
    public CountryDetailsServiceImpl(CountryRepository countryRepository,
                                     S3Service s3Service) {
        this.countryRepository = countryRepository;
        this.s3Service = s3Service;
    }



    @Override
    public ResponseEntity<GetCountryWithCountryDetailsDto> getSingleCountryDetails(int countryId) {
        return ResponseEntity.ok(getCountryWithDetailsByCountryIdFromDB(countryId));
    }

    private GetCountryWithCountryDetailsDto getCountryWithDetailsByCountryIdFromDB(int countryId) {
        return countryRepository.findCountryWithCountryDetailsByCountryId(countryId)
                .orElseThrow(()-> new EntityNotFoundException("No such country has this id."));
    }

    @Transactional
    @Override
    public ResponseEntity<String> editCountryDetails(int countryId, EditCountryDetailsDto editCountryDetailsDto) {
        if(!checkIfSentEmptyData(editCountryDetailsDto)){
            return badRequestException("you sent an empty data to change");
        }

        CountryEntity country = getCountryById(countryId);

        CountryDetailsEntity countryDetails = country.getCountryDetails();

        updateToNewData(country.getCountryDetails(), editCountryDetailsDto);

        countryRepository.save(country);



        return ResponseEntity.ok
        (
            new GetCountryWithCountryDetailsDto(
                countryDetails.getId(),
                country.getCountry(),
                country.getMainImage(),
                countryDetails.getImageOne(),
                countryDetails.getImageTwo(),
                countryDetails.getImageThree(),
                countryDetails.getDescription()
        ).toString());
    }

    private CountryEntity getCountryById(int countryId){
        return countryRepository.findById(countryId)
                .orElseThrow(()-> new EntityNotFoundException("No such country has this id."));
    }

    private void updateToNewData(CountryDetailsEntity countryDetails, EditCountryDetailsDto editCountryDetailsDto) {
        if(editCountryDetailsDto.getImageOne() != null){
            s3Service.deleteFile(countryDetails.getImageOne());
            String countryDetailsImageOneName = s3Service.uploadFile(editCountryDetailsDto.getImageOne());
            countryDetails.setImageOne(countryDetailsImageOneName);
        }
        if(editCountryDetailsDto.getImageTwo() != null){
            s3Service.deleteFile(countryDetails.getImageTwo());
            String countryDetailsImageTwoName = s3Service.uploadFile(editCountryDetailsDto.getImageTwo());
            countryDetails.setImageTwo(countryDetailsImageTwoName);
        }
        if(editCountryDetailsDto.getImageThree() != null){
            s3Service.deleteFile(countryDetails.getImageThree());
            String countryDetailsImageThreeName = s3Service.uploadFile(editCountryDetailsDto.getImageThree());
            countryDetails.setImageThree(countryDetailsImageThreeName);
        }
        if(editCountryDetailsDto.getDescription() != null){
            countryDetails.setDescription(editCountryDetailsDto.getDescription());
        }
    }
}


