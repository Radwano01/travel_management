package com.hackathon.backend.package_.services;

import com.hackathon.backend.dto.packageDto.EditPackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.PackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.PostPackageEvaluationDto;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.services.package_.PackageEvaluationService;
import com.hackathon.backend.utilities.user.UserUtils;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void addComment() throws ExecutionException, InterruptedException {
        //given
        int packageId = 1;
        long userId = 1L;

        PostPackageEvaluationDto postPackageEvaluationDto = new PostPackageEvaluationDto();
        postPackageEvaluationDto.setComment("testComment");
        postPackageEvaluationDto.setRate(4);

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);
        UserEntity user = new UserEntity();
        user.setId(userId);

        //behavior
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);
        when(userUtils.findById(userId)).thenReturn(user);

        //when
        CompletableFuture<ResponseEntity<String>> response = packageEvaluationService.addComment(packageId, userId, postPackageEvaluationDto);

        //then
        assertEquals(HttpStatus.OK, response.get().getStatusCode());
    }

    @Test
    void getComments() throws ExecutionException, InterruptedException {
        //given
        int packageId = 1;

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("testUsername");
        user.setImage("testImage");


        PackageEvaluationEntity packageEvaluation = new PackageEvaluationEntity();
        packageEvaluation.setComment("testComment");
        packageEvaluation.setRate(4);
        packageEvaluation.setUser(user);

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);
        packageEntity.getPackageEvaluations().add(packageEvaluation);

        packageEvaluation.setPackageEntity(packageEntity);

        //behavior
        when(packageUtils.findById(packageId)).thenReturn(packageEntity);

        //when
        CompletableFuture<ResponseEntity<?>> response = packageEvaluationService.getComments(packageId);
        List<PackageEvaluationDto> responseData = (List<PackageEvaluationDto>) response.get().getBody();
        //then
        assertEquals(HttpStatus.OK, response.get().getStatusCode());
        assertNotNull(responseData);
        assertEquals(packageEvaluation.getComment(), responseData.get(0).getComment());
        assertEquals(packageEvaluation.getRate(), responseData.get(0).getRate());
        assertEquals(packageEvaluation.getUser().getUsername(), user.getUsername());
        assertEquals(packageEvaluation.getUser().getImage(), user.getImage());
    }

    @Test
    void editComment() throws ExecutionException, InterruptedException {
        // given
        long commentId = 1L;
        EditPackageEvaluationDto editPackageEvaluationDto = new EditPackageEvaluationDto();
        editPackageEvaluationDto.setComment("testComment");
        editPackageEvaluationDto.setRate(2);

        PackageEvaluationEntity packageEvaluation = new PackageEvaluationEntity("testComment1", 3,
                new UserEntity(), new PackageEntity());
        packageEvaluation.setId(commentId);

        // behavior
        when(packageEvaluationUtils.findById(commentId)).thenReturn(packageEvaluation);
        when(packageEvaluationUtils.checkHelper(editPackageEvaluationDto)).thenReturn(true);

        doAnswer(invocation -> {
            PackageEvaluationEntity entity = invocation.getArgument(0);
            EditPackageEvaluationDto dto = invocation.getArgument(1);

            entity.setComment(dto.getComment());
            entity.setRate(dto.getRate());

            return null;
        }).when(packageEvaluationUtils).editHelper(any(PackageEvaluationEntity.class), any(EditPackageEvaluationDto.class));

        // when
        CompletableFuture<ResponseEntity<String>> response = packageEvaluationService.editComment(commentId, editPackageEvaluationDto);

        // then
        assertEquals(HttpStatus.OK, response.get().getStatusCode());
        assertEquals(editPackageEvaluationDto.getComment(), packageEvaluation.getComment());
        assertEquals(editPackageEvaluationDto.getRate(), packageEvaluation.getRate());
        verify(packageEvaluationUtils).save(packageEvaluation);
    }

    @Test
    void removeComment() throws ExecutionException, InterruptedException {
        //given
        int packageId = 1;
        long userId = 1L;
        long commentId = 1L;

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);

        UserEntity user = new UserEntity();
        user.setId(userId);

        PackageEvaluationEntity packageEvaluation = new PackageEvaluationEntity("testComment", 4, user, packageEntity);

        //behavior
        when(packageEvaluationUtils.findById(commentId)).thenReturn(packageEvaluation);

        //when
        CompletableFuture<ResponseEntity<String>> response = packageEvaluationService.removeComment(commentId);

        //then
        assertEquals(HttpStatus.OK, response.get().getStatusCode());
        verify(packageUtils).save(packageEntity);
        verify(userUtils).save(user);
        verify(packageEvaluationUtils).delete(packageEvaluation);
    }
}