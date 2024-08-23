package com.hackathon.backend.package_.services;

import com.hackathon.backend.dto.packageDto.CreatePackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.EditPackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.PackageEvaluationDto;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import com.hackathon.backend.services.package_.PackageEvaluationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackageEvaluationServiceTest {

    @Mock
    PackageRepository packageRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    PackageEvaluationService packageEvaluationService;

    @Test
    void addComment_ShouldAddComment_WhenAllDataIsProvided() {
        // given
        int packageId = 1;
        long userId = 1L;
        CreatePackageEvaluationDto createPackageEvaluationDto = new CreatePackageEvaluationDto();
        createPackageEvaluationDto.setComment("Great package!");
        createPackageEvaluationDto.setRate(5);

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setId(packageId);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        //behavior
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // when
        CompletableFuture<ResponseEntity<?>> responseFuture = packageEvaluationService.addComment(packageId, userId, createPackageEvaluationDto);
        ResponseEntity<?> response = responseFuture.join();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment added successfully", response.getBody());
        assertEquals(1, packageEntity.getPackageEvaluations().size());
        verify(packageRepository).save(packageEntity);
    }


    @Test
    void getComments_ShouldReturnComments_WhenPackageExists() {
        // given
        int packageId = 1;

        PackageEvaluationDto commentDto = new PackageEvaluationDto(
                1L,
                "Great package!",
                5,
                1L,
                "username",
                "https://example.com/user.jpg"
        );
        List<PackageEvaluationDto> comments = List.of(commentDto);

        //behavior
        when(packageRepository.findAllEvaluationsPackageByPackageId(packageId)).thenReturn(comments);

        // when
        CompletableFuture<ResponseEntity<?>> responseFuture = packageEvaluationService.getComments(packageId);
        ResponseEntity<?> response = responseFuture.join();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comments, response.getBody());
        verify(packageRepository).findAllEvaluationsPackageByPackageId(packageId);
    }

    @Test
    void editComment_ShouldUpdateComment_WhenValidDataIsProvided() {
        // given
        int packageId = 1;
        long commentId = 1L;
        EditPackageEvaluationDto editPackageEvaluationDto = new EditPackageEvaluationDto("Updated comment", 4);

        PackageEntity packageEntity = new PackageEntity();
        PackageEvaluationEntity packageEvaluation = new PackageEvaluationEntity("Original comment", 5, new UserEntity(), packageEntity);
        packageEvaluation.setId(commentId);
        packageEntity.getPackageEvaluations().add(packageEvaluation);

        //behavior
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));

        // when
        CompletableFuture<ResponseEntity<?>> responseFuture = packageEvaluationService.editComment(packageId, commentId, editPackageEvaluationDto);
        ResponseEntity<?> response = responseFuture.join();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated comment", packageEvaluation.getComment());
        assertEquals(4, packageEvaluation.getRate());
        verify(packageRepository).save(packageEntity);
    }

    @Test
    void editComment_ShouldReturnBadRequest_WhenEmptyDataIsSent() {
        // given
        int packageId = 1;
        long commentId = 1L;

        EditPackageEvaluationDto emptyDto = new EditPackageEvaluationDto();

        // when
        CompletableFuture<ResponseEntity<?>> responseFuture = packageEvaluationService.editComment(packageId, commentId, emptyDto);
        ResponseEntity<?> response = responseFuture.join();

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("you sent an empty data to change", response.getBody());
    }


    @Test
    void removeComment_ShouldDeleteComment_WhenValidDataIsProvided() {
        // given
        int packageId = 1;
        long commentId = 1L;

        PackageEntity packageEntity = new PackageEntity();
        PackageEvaluationEntity packageEvaluation = new PackageEvaluationEntity("Original comment", 5, new UserEntity(), packageEntity);
        packageEvaluation.setId(commentId);
        packageEntity.getPackageEvaluations().add(packageEvaluation);

        //behavior
        when(packageRepository.findById(packageId)).thenReturn(Optional.of(packageEntity));

        // when
        CompletableFuture<ResponseEntity<?>> responseFuture = packageEvaluationService.removeComment(packageId, commentId);
        ResponseEntity<?> response = responseFuture.join();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment deleted successfully", response.getBody());
        assertEquals(0, packageEntity.getPackageEvaluations().size());
        verify(packageRepository).save(packageEntity);
    }
}