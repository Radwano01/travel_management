package com.hackathon.backend.package_.repositories.features;

import com.hackathon.backend.dto.packageDto.features.GetBenefitDto;
import com.hackathon.backend.dto.packageDto.features.GetRoadmapDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.entities.package_.packageFeatures.RoadmapEntity;
import com.hackathon.backend.repositories.package_.PackageDetailsRepository;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.BenefitRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.RoadmapRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PackageDetailsRepositoryTest {

    @Autowired
    PackageDetailsRepository packageDetailsRepository;

    @Autowired
    PackageRepository packageRepository;

    @Autowired
    RoadmapRepository roadmapRepository;

    @Autowired
    BenefitRepository benefitRepository;

    private PackageEntity packageEntity;

    @BeforeEach
    void setUp() {
        // Create and save PackageEntity
        packageEntity = new PackageEntity();
        packageEntity.setPackageName("Test Package");
        packageEntity.setPrice(100);
        packageEntity.setRate(4);
        packageEntity.setMainImage("mainImage.jpg");
        packageRepository.save(packageEntity);

        // Create PackageDetailsEntity and associate with PackageEntity
        PackageDetailsEntity packageDetails = new PackageDetailsEntity();
        packageDetails.setImageOne("imageOne.jpg");
        packageDetails.setImageTwo("imageTwo.jpg");
        packageDetails.setImageThree("imageThree.jpg");
        packageDetails.setDescription("Package description");
        packageDetails.setPackageOffer(packageEntity);  // Associate the package

        // Create and save RoadmapEntity and BenefitEntity
        RoadmapEntity roadmapEntity = new RoadmapEntity("Roadmap 1");
        roadmapRepository.save(roadmapEntity);
        packageDetails.getRoadmaps().add(roadmapEntity);

        BenefitEntity benefitEntity = new BenefitEntity("Benefit 1");
        benefitRepository.save(benefitEntity);
        packageDetails.getBenefits().add(benefitEntity);

        // Save PackageDetailsEntity
        packageDetailsRepository.save(packageDetails);
    }

    @AfterEach
    void tearDown() {
        roadmapRepository.deleteAll();
        benefitRepository.deleteAll();
        packageRepository.deleteAll();
    }

    @Test
    void itShouldFindRoadmapsByPackageId() {
        // given
        int packageId = packageEntity.getId();

        // when
        List<GetRoadmapDto> roadmaps = packageDetailsRepository.findPackageDetailsRoadmapsByPackageId(packageId);

        // then
        assertNotNull(roadmaps);
        assertEquals(1, roadmaps.size());
        assertEquals("Roadmap 1", roadmaps.get(0).getRoadmap());
    }

    @Test
    void itShouldFindBenefitsByPackageId() {
        // given
        int packageId = packageEntity.getId();

        // when
        List<GetBenefitDto> benefits = packageDetailsRepository.findPackageDetailsBenefitsByPackageId(packageId);

        // then
        assertNotNull(benefits);
        assertEquals(1, benefits.size());
        assertEquals("Benefit 1", benefits.get(0).getBenefit());
    }

}