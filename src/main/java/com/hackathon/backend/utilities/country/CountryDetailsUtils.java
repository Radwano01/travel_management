package com.hackathon.backend.utilities.country;

import com.hackathon.backend.dto.countryDto.EditCountryDetailsDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.repositories.country.CountryDetailsRepository;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CountryDetailsUtils {

    private final CountryDetailsRepository countryDetailsRepository;
    private final S3Service s3Service;

    @Autowired
    public CountryDetailsUtils(CountryDetailsRepository countryDetailsRepository,
                               S3Service s3Service) {
        this.countryDetailsRepository = countryDetailsRepository;
        this.s3Service = s3Service;
    }

    public void save(CountryDetailsEntity countryDetails) {
        countryDetailsRepository.save(countryDetails);
    }

    public void delete(CountryDetailsEntity countryDetails) {
        countryDetailsRepository.delete(countryDetails);
    }

    public boolean checkHelper(EditCountryDetailsDto editCountryDetailsDto){
        return  editCountryDetailsDto.getImageOne() != null ||
                editCountryDetailsDto.getImageTwo() != null ||
                editCountryDetailsDto.getImageThree() != null ||
                editCountryDetailsDto.getDescription() != null;
    }

    public void editHelper(CountryDetailsEntity countryDetails,
                            EditCountryDetailsDto editCountryDetailsDto) {
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
