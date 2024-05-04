package com.hackathon.backend.package_.repositories;

import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.repositories.package_.PackageDetailsRepository;
import com.hackathon.backend.repositories.package_.PackageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PackageDetailsRepositoryTest {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private PackageDetailsRepository packageDetailsRepository;

    @Test
    void testDeleteByPackageOfferId() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(1);
        packageRepository.save(packageEntity);

        PackageDetailsEntity packageDetails = new PackageDetailsEntity();
        packageDetails.setPackageOffer(packageEntity);
        packageDetailsRepository.save(packageDetails);

        //when
        packageDetailsRepository.deleteByPackageOfferId(1);

        //then
        assertTrue(packageDetailsRepository.findById(1).isEmpty());
    }
}