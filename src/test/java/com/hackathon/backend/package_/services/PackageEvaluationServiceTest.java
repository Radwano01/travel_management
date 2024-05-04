package com.hackathon.backend.package_.services;

import com.hackathon.backend.dto.packageDto.PackageEvaluationDto;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.services.package_.PackageEvaluationService;
import com.hackathon.backend.utilities.UserUtils;
import com.hackathon.backend.utilities.package_.PackageEvaluationUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PackageEvaluationServiceTest {


    @Mock
    private PackageUtils packageUtils;

    @Mock
    private UserUtils userUtils;

    @Mock
    private PackageEvaluationUtils packageEvaluationUtils;

    private PackageEvaluationService packageEvaluationService;

    @BeforeEach
    void setUp() {
        packageEvaluationService = new PackageEvaluationService(
                packageUtils,
                userUtils,
                packageEvaluationUtils
        );
    }

    @Test
    void addComment() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageEvaluations(new ArrayList<>());
        UserEntity userEntity = new UserEntity();

        when(userUtils.findById(1L)).thenReturn(userEntity);
        when(packageUtils.findById(1)).thenReturn(packageEntity);
        //when
        ResponseEntity<?> response = packageEvaluationService
                .addComment(1, 1L, new PackageEvaluationDto());

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getComments() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        List<PackageEvaluationEntity> packageEvaluations = new ArrayList<>();
        packageEvaluations.add(new PackageEvaluationEntity());
        packageEntity.setPackageEvaluations(packageEvaluations);

        when(packageUtils.findById(1)).thenReturn(packageEntity);
        //when
        ResponseEntity<?> response = packageEvaluationService.getComments(1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editComment() {
        //given
        PackageEvaluationEntity packageEvaluationEntity = new PackageEvaluationEntity();
        when(packageEvaluationUtils.findById(1L)).thenReturn(packageEvaluationEntity);

        PackageEvaluationDto packageEvaluationDto = new PackageEvaluationDto();
        packageEvaluationDto.setComment("Edited comment");
        packageEvaluationDto.setRate(5);

        //when
        ResponseEntity<?> response = packageEvaluationService
                .editComment(1L, packageEvaluationDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void removeComment() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        UserEntity userEntity = new UserEntity();
        PackageEvaluationEntity packageEvaluationEntity = new PackageEvaluationEntity();

        when(packageUtils.findById(1)).thenReturn(packageEntity);
        when(userUtils.findById(1L)).thenReturn(userEntity);
        when(packageEvaluationUtils.findById(1L)).thenReturn(packageEvaluationEntity);
        //when
        ResponseEntity<?> response = packageEvaluationService
                .removeComment(1, 1L, 1L);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}