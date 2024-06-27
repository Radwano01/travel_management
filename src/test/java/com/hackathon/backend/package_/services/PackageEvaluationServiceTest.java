package com.hackathon.backend.package_.services;

import com.hackathon.backend.dto.hotelDto.HotelEvaluationDto;
import com.hackathon.backend.dto.packageDto.EditPackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.PackageEvaluationDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.services.package_.PackageEvaluationService;
import com.hackathon.backend.utilities.UserUtils;
import com.hackathon.backend.utilities.package_.PackageEvaluationUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackageEvaluationServiceTest {

    @Mock
    PackageUtils packageUtils;

    @Mock
    UserUtils userUtils;

    @Mock
    PackageEvaluationUtils packageEvaluationUtils;

    @InjectMocks
    PackageEvaluationService packageEvaluationService;

    @Test
    void addComment() {
        //given
        int packageId = 1;
        long userId = 1L;

        PackageEvaluationDto packageEvaluationDto = new PackageEvaluationDto();
        packageEvaluationDto.setComment("testComment");
        packageEvaluationDto.setRate(4.5f);

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);
        UserEntity user = new UserEntity();
        user.setId(userId);

        //behavior
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);
        when(userUtils.findById(userId)).thenReturn(user);

        //when
        ResponseEntity<?> response = packageEvaluationService.addComment(packageId, userId, packageEvaluationDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getComments() {
        //given
        int packageId = 1;

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("testUsername");
        user.setImage("testImage");


        PackageEvaluationEntity packageEvaluation = new PackageEvaluationEntity();
        packageEvaluation.setComment("testComment");
        packageEvaluation.setRate(4.0f);
        packageEvaluation.setUser(user);

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);
        packageEntity.getPackageEvaluations().add(packageEvaluation);

        packageEvaluation.setPackageEntity(packageEntity);

        //behavior
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);

        //when
        ResponseEntity<?> response = packageEvaluationService.getComments(packageId);
        List<PackageEvaluationDto> responseData = (List<PackageEvaluationDto>) response.getBody();
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseData);
        assertEquals(packageEvaluation.getComment(), responseData.get(0).getComment());
        assertEquals(packageEvaluation.getRate(), responseData.get(0).getRate());
        assertEquals(packageEvaluation.getUser().getUsername(), user.getUsername());
        assertEquals(packageEvaluation.getUser().getImage(), user.getImage());
    }

    @Test
    void editComment() {
        //given
        long commentId = 1L;
        EditPackageEvaluationDto editPackageEvaluationDto = new EditPackageEvaluationDto();
        editPackageEvaluationDto.setComment("testComment");
        editPackageEvaluationDto.setRate(2.50f);
        PackageEvaluationEntity packageEvaluation = new PackageEvaluationEntity("testComment1", 3.5f,
                new UserEntity(), new PackageEntity());

        //behavior
        when(packageEvaluationUtils.findById(commentId)).thenReturn(packageEvaluation);

        //when
        ResponseEntity<?> response = packageEvaluationService.editComment(commentId, editPackageEvaluationDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(editPackageEvaluationDto.getComment(), packageEvaluation.getComment());
        assertEquals(editPackageEvaluationDto.getRate(), packageEvaluation.getRate());
        verify(packageEvaluationUtils).save(packageEvaluation);
    }

    @Test
    void removeComment() {
        //given
        int packageId = 1;
        long userId = 1L;
        long commentId = 1L;

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);

        UserEntity user = new UserEntity();
        user.setId(userId);

        PackageEvaluationEntity packageEvaluation = new PackageEvaluationEntity("testComment", 4.0f, user, packageEntity);

        //behavior
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);
        when(userUtils.findById(userId)).thenReturn(user);
        when(packageEvaluationUtils.findById(commentId)).thenReturn(packageEvaluation);

        //when
        ResponseEntity<?> response = packageEvaluationService.removeComment(packageId, userId, commentId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(packageUtils).save(packageEntity);
        verify(userUtils).save(user);
        verify(packageEvaluationUtils).delete(packageEvaluation);
    }
}