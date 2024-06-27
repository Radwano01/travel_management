package com.hackathon.backend.utilities.country;


import com.hackathon.backend.dto.countryDto.EditCountryDto;
import com.hackathon.backend.dto.countryDto.GetCountryDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountryUtils {

    private final CountryRepository countryRepository;
    private final S3Service s3Service;

    @Autowired
    public CountryUtils(CountryRepository countryRepository,
                        S3Service s3Service) {
        this.countryRepository = countryRepository;
        this.s3Service = s3Service;
    }

    public CountryEntity findCountryById(@NonNull int countryId){
        return countryRepository.findById(countryId)
                .orElseThrow(()-> new EntityNotFoundException("Country id is not found"));
    }

    public boolean existsByCountry(String countryName) {
        return countryRepository.existsByCountry(countryName);
    }

    public void save(CountryEntity country) {
        countryRepository.save(country);
    }

    public List<GetCountryDto> findAllCountries() {
        return countryRepository.findAllCountries();
    }

    public void delete(CountryEntity countryEntity) {
        countryRepository.delete(countryEntity);
    }

    public boolean checkHelper(EditCountryDto editCountryDto){
        return  editCountryDto.getCountry() != null ||
                editCountryDto.getMainImage() != null;
    }

    public void editHelper(CountryEntity country,
                            EditCountryDto editCountryDto) {
        if (editCountryDto.getCountry() != null) {
            boolean existsCountry = existsByCountry(editCountryDto.getCountry());
            if(!existsCountry) country.setCountry(editCountryDto.getCountry());
        }
        if (editCountryDto.getMainImage() != null) {
            s3Service.deleteFile(country.getMainImage());
            String newMainImageName = s3Service.uploadFile(editCountryDto.getMainImage());
            country.setMainImage(newMainImageName);
        }
    }
}
