package com.hackathon.backend.country.repositories;

import com.hackathon.backend.dto.countryDto.GetCountryDto;
import com.hackathon.backend.dto.countryDto.GetCountryWithCountryDetailsDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto;
import com.hackathon.backend.dto.packageDto.GetEssentialPackageDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CountryRepositoryTest {

    @Autowired
    CountryRepository countryRepository;

    CountryEntity country;

    @BeforeEach
    void setUp() {
        country = new CountryEntity();
        country.setCountry("test-country");
        country.setMainImage("test.png");

        countryRepository.save(country);
    }

    @AfterEach
    void tearDown(){
        countryRepository.deleteAll();
    }

    @Test
    void itShouldReturnExistCountryByCountry() {
        //given
        String countryName = "test-country";

        //when
        boolean response = countryRepository.existsByCountry(countryName);

        //then
        assertTrue(response);
    }

    @Test
    void itShouldReturnNotFoundCountryByCountry() {
        //given
        String countryName = "country";

        //when
        boolean response = countryRepository.existsByCountry(countryName);

        //then
        assertFalse(response);
    }

    @Test
    void itShouldReturnAllCountries() {

        //when
        List<GetCountryDto> response = countryRepository.findAllCountries();

        //then
        assertNotNull(response);
    }


    @Test
    void itShouldReturnCountryWithCountryDetailsByCountryId() {

        //when
        Optional<GetCountryWithCountryDetailsDto> response = countryRepository
                .findCountryWithCountryDetailsByCountryId(country.getId());

        //then
        assertNotNull(response);
    }

    @Test
    void itShouldReturnPackagesByCountryId() {

        //when
        List<GetEssentialPackageDto> response = countryRepository.findPackagesByCountryId(country.getId());

        //then
        assertNotNull(response);
    }

    @Test
    void itShouldReturnPackageByCountryIdAndPackageId() {

        //when
        Optional<PackageEntity> response = countryRepository.findPackageByCountryIdAndPackageId(country.getId(), 1);

        //then
        assertNotNull(response);
    }

    @Test
    void itShouldReturnEssentialPlacesDataByCountryId() {

        //when
        List<GetEssentialPlaceDto> response = countryRepository.findEssentialPlacesDataByCountryId(country.getId());

        //then
        assertNotNull(response);
    }

    @Test
    void itShouldReturnPlaceByCountryIdANDPlaceId() {

        //when
        Optional<PlaceEntity> response = countryRepository.findPlaceByCountryIdANDPlaceId(country.getId(), 1);

        //then
        assertNotNull(response);
    }
}