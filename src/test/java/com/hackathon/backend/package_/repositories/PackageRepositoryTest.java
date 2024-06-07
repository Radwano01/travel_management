package com.hackathon.backend.package_.repositories;

import com.hackathon.backend.dto.packageDto.GetEssentialPackageDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.repositories.package_.PackageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PackageRepositoryTest {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    PackageRepository packageRepository;

    @BeforeEach
    void setUp(){
        CountryEntity country = new CountryEntity(
                "testCountry",
                "testImage"
        );
        countryRepository.save(country);

        PackageEntity packageEntity = new PackageEntity(
                "testPackage",
                200,
                "testImage",
                country
        );
        packageRepository.save(packageEntity);
    }

    @AfterEach
    void tearDown(){
        packageRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    void findPackagesByCountryId() {
        //given
        int countryId = countryRepository.findAll().get(0).getId();

        //when
        List<GetEssentialPackageDto> response = packageRepository.findPackagesByCountryId(countryId);

        //then
        assertEquals(response.get(0).getPackageName(), "testPackage");
        assertEquals(response.get(0).getMainImage(), "testImage");
    }
}