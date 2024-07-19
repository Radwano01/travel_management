package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.packageDto.EditPackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.PackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.PostPackageEvaluationDto;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.utilities.user.UserUtils;
import com.hackathon.backend.utilities.package_.PackageEvaluationUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PackageEvaluationService {

    private final PackageUtils packageUtils;
    private final UserUtils userUtils;
    private final PackageEvaluationUtils packageEvaluationUtils;

    @Autowired
    public PackageEvaluationService(PackageUtils packageUtils,
                                    UserUtils userUtils,
                                    PackageEvaluationUtils packageEvaluationUtils) {
        this.packageUtils = packageUtils;
        this.userUtils = userUtils;
        this.packageEvaluationUtils = packageEvaluationUtils;
    }

    @Async("commentTaskExecutor")
    @Transactional
    public CompletableFuture<ResponseEntity<String>> addComment(int packageId,
                                                                long userId,
                                                                @NonNull PostPackageEvaluationDto postPackageEvaluationDto) {
        try {
            PackageEntity packageEntity = packageUtils.findById(packageId);
            UserEntity user = userUtils.findById(userId);
            PackageEvaluationEntity packageEvaluation = new PackageEvaluationEntity(
                    postPackageEvaluationDto.getComment(),
                    postPackageEvaluationDto.getRate(),
                    user,
                    packageEntity

            );
            packageEvaluationUtils.save(packageEvaluation);

            packageEntity.getPackageEvaluations().add(packageEvaluation);
            packageUtils.save(packageEntity);

            user.getPackageEvaluations().add(packageEvaluation);
            userUtils.save(user);
            return CompletableFuture.completedFuture(ResponseEntity.ok("Comment added successfully"));
        } catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    public CompletableFuture<ResponseEntity<?>> getComments(int packageId) {
        try {
            PackageEntity packageEntity = packageUtils.findById(packageId);

            List<PackageEvaluationEntity> packageEvaluations = packageEntity.getPackageEvaluations();


            List<PackageEvaluationDto> packageEvaluationDtoList = new ArrayList<>();

            for(PackageEvaluationEntity packageEvaluation:packageEvaluations){
                PackageEvaluationDto packageEvaluationDto = new PackageEvaluationDto(
                        packageEvaluation.getId(),
                        packageEvaluation.getComment(),
                        packageEvaluation.getRate(),
                        packageEvaluation.getUser().getId(),
                        packageEvaluation.getUser().getUsername(),
                        packageEvaluation.getUser().getImage()
                );
                packageEvaluationDtoList.add(packageEvaluationDto);
            }

            return CompletableFuture.completedFuture(ResponseEntity.ok(packageEvaluationDtoList));
        } catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @Async("commentTaskExecutor")
    @Transactional
    public CompletableFuture<ResponseEntity<String>> editComment(long commentId,
                                              EditPackageEvaluationDto editPackageEvaluationDto) {
        try {
            if(!packageEvaluationUtils.checkHelper(editPackageEvaluationDto)){
                return CompletableFuture.completedFuture(badRequestException("you sent an empty data to change"));
            }
            PackageEvaluationEntity packageEvaluation = packageEvaluationUtils.findById(commentId);
            packageEvaluationUtils.editHelper(packageEvaluation, editPackageEvaluationDto);
            packageEvaluationUtils.save(packageEvaluation);
            return CompletableFuture.completedFuture(ResponseEntity.ok("Comment updated successfully"));
        } catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @Async("commentTaskExecutor")
    @Transactional
    public CompletableFuture<ResponseEntity<String>> removeComment(long commentId) {
        try {
            PackageEvaluationEntity packageEvaluation = packageEvaluationUtils.findById(commentId);
            if(packageEvaluation == null){
                return CompletableFuture.completedFuture(badRequestException("Comment is not found"));
            }
            PackageEntity packageEntity = packageEvaluation.getPackageEntity();
            UserEntity user = packageEvaluation.getUser();

            packageEntity.getPackageEvaluations().remove(packageEvaluation);
            packageUtils.save(packageEntity);

            user.getPackageEvaluations().remove(packageEvaluation);
            userUtils.save(user);

            packageEvaluationUtils.delete(packageEvaluation);
            return CompletableFuture.completedFuture(ResponseEntity.ok("Comment deleted successfully"));
        } catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }
}
