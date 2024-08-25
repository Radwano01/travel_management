package com.hackathon.backend.package_.services.features;

import com.hackathon.backend.dto.packageDto.features.CreateBenefitDto;
import com.hackathon.backend.dto.packageDto.features.EditBenefitDto;
import com.hackathon.backend.dto.packageDto.features.GetBenefitDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.repositories.package_.PackageDetailsRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.BenefitRepository;
import com.hackathon.backend.services.package_.packageFeatures.impl.BenefitServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BenefitServiceImplTest {

    @Mock
    BenefitRepository benefitRepository;

    @Mock
    PackageDetailsRepository packageDetailsRepository;

    @InjectMocks
    BenefitServiceImpl benefitServiceImpl;

    @Test
    void createBenefit_ShouldReturnSuccess_WhenBenefitIsValid() {
        // given
        CreateBenefitDto createBenefitDto = new CreateBenefitDto();
        createBenefitDto.setBenefit("Free WiFi");

        //behavior
        when(benefitRepository.existsBenefitByBenefit("Free WiFi")).thenReturn(false);

        // when
        ResponseEntity<String> response = benefitServiceImpl.createBenefit(createBenefitDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Benefit created successfullyFree WiFi", response.getBody());
    }

    @Test
    void createBenefit_ShouldReturnBadRequest_WhenBenefitAlreadyExists() {
        // given
        CreateBenefitDto createBenefitDto = new CreateBenefitDto();
        createBenefitDto.setBenefit("Free WiFi");

        //behavior
        when(benefitRepository.existsBenefitByBenefit("Free WiFi")).thenReturn(true);

        // when
        ResponseEntity<String> response = benefitServiceImpl.createBenefit(createBenefitDto);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Hotel Feature already exists", response.getBody());
    }

    @Test
    void getBenefits_ShouldReturnAllBenefits() {
        // given
        List<GetBenefitDto> benefits = List.of(
                new GetBenefitDto(1, "Free Breakfast"),
                new GetBenefitDto(2, "Gym Access")
        );

        //behavior
        when(benefitRepository.findAllBenefits()).thenReturn(benefits);

        // when
        ResponseEntity<List<GetBenefitDto>> response = benefitServiceImpl.getBenefits();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(benefits, response.getBody());
    }


    @Test
    void editBenefit_ShouldReturnSuccess_WhenBenefitIsValid() {
        // given
        int benefitId = 1;
        EditBenefitDto editBenefitDto = new EditBenefitDto();
        editBenefitDto.setBenefit("Updated Benefit");

        BenefitEntity benefitEntity = new BenefitEntity("Old Benefit");

        //behavior
        when(benefitRepository.findById(benefitId)).thenReturn(Optional.of(benefitEntity));

        // when
        ResponseEntity<String> response = benefitServiceImpl.editBenefit(benefitId, editBenefitDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Benefit edit successfullyUpdated Benefit", response.getBody());
        assertEquals("Updated Benefit", benefitEntity.getBenefit());
        verify(benefitRepository).save(benefitEntity);
    }

    @Test
    void editBenefit_ShouldReturnBadRequest_WhenBenefitIsEmpty() {
        // given
        int benefitId = 1;
        EditBenefitDto editBenefitDto = new EditBenefitDto();

        // when
        ResponseEntity<String> response = benefitServiceImpl.editBenefit(benefitId, editBenefitDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("you sent an empty data to change", response.getBody());
    }


    @Test
    void deleteBenefit_ShouldReturnSuccess_WhenBenefitExists() {
        // given
        int benefitId = 1;
        BenefitEntity benefitEntity = new BenefitEntity("Benefit to Delete");

        PackageDetailsEntity packageDetails = new PackageDetailsEntity();
        packageDetails.getBenefits().add(benefitEntity);
        benefitEntity.setPackageDetails(List.of(packageDetails));

        //behavior
        when(benefitRepository.findById(benefitId)).thenReturn(Optional.of(benefitEntity));

        // when
        ResponseEntity<String> response = benefitServiceImpl.deleteBenefit(benefitId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Benefit deleted successfully", response.getBody());
        verify(benefitRepository).delete(benefitEntity);
        verify(packageDetailsRepository).save(packageDetails);
    }
}