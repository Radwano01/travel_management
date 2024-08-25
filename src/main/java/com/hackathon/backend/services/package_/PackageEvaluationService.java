package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.packageDto.CreatePackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.EditPackageEvaluationDto;
import io.micrometer.common.lang.NonNull;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

public interface PackageEvaluationService {

    CompletableFuture<ResponseEntity<?>> addComment
            (int packageId, long userId, @NonNull CreatePackageEvaluationDto createPackageEvaluationDto);

    CompletableFuture<ResponseEntity<?>> getComments(int packageId);

    CompletableFuture<ResponseEntity<?>> editComment
            (int packageId, long commentId, EditPackageEvaluationDto editPackageEvaluationDto);

    CompletableFuture<ResponseEntity<?>> removeComment(int packageId, long commentId);
}
