package com.hackathon.backend.package_.repositories;

import com.hackathon.backend.dto.packageDto.EssentialPackageDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.repositories.package_.PackageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PackageRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private PackageRepository packageRepository;

    @Test
    void testFindPackagesByCountryId() {
        //given
        CountryEntity country = new CountryEntity();
        country.setId(1);

        //when
        List<EssentialPackageDto> packages = packageRepository.findPackagesByCountryId(country.getId());

        //then
        assertNotNull(packages);
    }
}