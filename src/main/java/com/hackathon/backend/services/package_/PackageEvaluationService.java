package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.packageDto.EditPackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.PackageEvaluationDto;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    @Transactional
    public ResponseEntity<String> addComment(int packageId,
                                        long userId,
                                        @NonNull PackageEvaluationDto packageEvaluationDto) {
        try {
            PackageEntity packageEntity = packageUtils.findById(packageId);
            UserEntity user = userUtils.findById(userId);
            PackageEvaluationEntity packageEvaluation = new PackageEvaluationEntity(
                    packageEvaluationDto.getComment(),
                    packageEvaluationDto.getRate(),
                    user,
                    packageEntity

            );
            packageEntity.getPackageEvaluations().add(packageEvaluation);
            packageUtils.save(packageEntity);
            userUtils.save(user);
            return ResponseEntity.ok("Comment added successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getComments(int packageId) {
        try {
            PackageEntity packageEntity = packageUtils.findById(packageId);
            List<PackageEvaluationEntity> packageEvaluations = packageEntity.getPackageEvaluations();
            List<PackageEvaluationDto> packageEvaluationDtos = packageEvaluations.stream()
                    .map((evaluation) -> {
                        if (evaluation.getUser() != null) {
                            return new PackageEvaluationDto(
                                    evaluation.getId(),
                                    evaluation.getComment(),
                                    evaluation.getRate(),
                                    evaluation.getUser().getUsername(),
                                    evaluation.getUser().getImage()
                            );
                        } else {
                            return null;
                        }
                    }).collect(Collectors.toList());
            return ResponseEntity.ok(packageEvaluationDtos);
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }


    @Transactional
    public ResponseEntity<String> editComment(long commentId,
                                         EditPackageEvaluationDto editPackageEvaluationDto) {
        try {
            if(!packageEvaluationUtils.checkHelper(editPackageEvaluationDto)){
                return badRequestException("you sent an empty data to change");
            }
            PackageEvaluationEntity packageEvaluation = packageEvaluationUtils.findById(commentId);
            packageEvaluationUtils.editHelper(packageEvaluation, editPackageEvaluationDto);
            packageEvaluationUtils.save(packageEvaluation);
            return ResponseEntity.ok("Comment updated successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> removeComment(int packageId, long userId, long commentId) {
        try {
            PackageEntity packageEntity = packageUtils.findById(packageId);
            UserEntity user = userUtils.findById(userId);
            PackageEvaluationEntity packageEvaluation = packageEvaluationUtils.findById(commentId);

            if (packageEntity != null && user != null && packageEvaluation != null) {
                if (packageEntity.getPackageEvaluations() == null) {
                    packageEntity.setPackageEvaluations(new ArrayList<>());
                }

                packageEntity.getPackageEvaluations().remove(packageEvaluation);

                if (user.getPackageEvaluations() == null) {
                    user.setPackageEvaluations(new ArrayList<>());
                }
                user.getPackageEvaluations().remove(packageEvaluation);

                packageUtils.save(packageEntity);
                userUtils.save(user);
                packageEvaluationUtils.delete(packageEvaluation);
                return ResponseEntity.ok("Comment deleted successfully");
            } else {
                return ResponseEntity.ok("Comment not found");
            }
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}
