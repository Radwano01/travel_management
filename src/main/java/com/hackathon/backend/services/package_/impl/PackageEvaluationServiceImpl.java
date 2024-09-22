package com.hackathon.backend.services.package_.impl;

import com.hackathon.backend.dto.packageDto.EditPackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.CreatePackageEvaluationDto;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import com.hackathon.backend.services.package_.PackageEvaluationService;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class PackageEvaluationServiceImpl implements PackageEvaluationService {

    private final PackageRepository packageRepository;
    private final UserRepository userRepository;

    @Autowired
    public PackageEvaluationServiceImpl(PackageRepository packageRepository,
                                        UserRepository userRepository) {
        this.packageRepository = packageRepository;
        this.userRepository = userRepository;
    }

    @Async("commentTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ResponseEntity<?>> addComment(int packageId, long userId,
                                                           @NonNull CreatePackageEvaluationDto createPackageEvaluationDto) {
        PackageEntity packageEntity = getPackageById(packageId);

        ResponseEntity<String> checkResult = checkIfUserAlreadyCommented(packageEntity, userId);
        if(!checkResult.getStatusCode().equals(HttpStatus.OK)){
            return CompletableFuture.completedFuture(checkResult);
        }

        UserEntity user = getUserById(userId);

        prepareANDSetEvaluationPackage(createPackageEvaluationDto, packageEntity, user);

        packageRepository.save(packageEntity);

        return CompletableFuture.completedFuture(ResponseEntity.ok("Comment added successfully"));
    }

    private PackageEntity getPackageById(int packageId){
        return packageRepository.findById(packageId)
                .orElseThrow(()-> new EntityNotFoundException("No such package has this id"));
    }

    private UserEntity getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new EntityNotFoundException("User id not found"));
    }

    private ResponseEntity<String> checkIfUserAlreadyCommented(PackageEntity packageEntity, long userId){
        for(PackageEvaluationEntity packageEvaluation : packageEntity.getPackageEvaluations()){
            UserEntity user = packageEvaluation.getUser();
            if(user != null && user.getId() == userId){
                return badRequestException("This user has already commented on this hotel");
            }
        }
        return ResponseEntity.ok("OK");
    }

    private void prepareANDSetEvaluationPackage(CreatePackageEvaluationDto createPackageEvaluationDto,
                                                PackageEntity packageEntity, UserEntity userEntity){
        PackageEvaluationEntity packageEvaluation = new PackageEvaluationEntity(
                createPackageEvaluationDto.getComment(),
                createPackageEvaluationDto.getRate(),
                userEntity,
                packageEntity

        );

        packageEntity.getPackageEvaluations().add(packageEvaluation);
    }

    @Override
    public CompletableFuture<ResponseEntity<?>> getComments(int packageId) {
        return CompletableFuture.completedFuture
                (ResponseEntity.ok(packageRepository.findAllEvaluationsPackageByPackageId(packageId)));
    }

    @Async("commentTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ResponseEntity<?>> editComment(int packageId, long commentId,
                              EditPackageEvaluationDto editPackageEvaluationDto) {
        if(!checkIfSentEmptyData(editPackageEvaluationDto)){
            return CompletableFuture.completedFuture(badRequestException("you sent an empty data to change"));
        }

        PackageEntity packageEntity = getPackageById(packageId);

        PackageEvaluationEntity packageEvaluation = getPackageEvaluationFromPackage(packageEntity, commentId);

        updateToNewData(packageEvaluation, editPackageEvaluationDto);

        packageRepository.save(packageEntity);

        return CompletableFuture.completedFuture(ResponseEntity.ok(packageEvaluation.toString()));
    }

    private PackageEvaluationEntity getPackageEvaluationFromPackage(PackageEntity packageEntity, long commentId){
        Optional<PackageEvaluationEntity> packageEvaluation = packageEntity.getPackageEvaluations()
                .stream().filter((data) -> data.getId() == commentId).findFirst();

        return packageEvaluation.orElse(null);
    }

    private void updateToNewData(PackageEvaluationEntity packageEvaluation,
                           EditPackageEvaluationDto editPackageEvaluationDto){
        if (editPackageEvaluationDto.getComment() != null) {
            packageEvaluation.setComment(editPackageEvaluationDto.getComment());
        }
        if (editPackageEvaluationDto.getRate() >= 0 && editPackageEvaluationDto.getRate() <= 5) {
            packageEvaluation.setRate(editPackageEvaluationDto.getRate());
        }
    }

    @Async("commentTaskExecutor")
    @Transactional
    @Override
    public CompletableFuture<ResponseEntity<?>> removeComment(int packageId, long commentId) {
        PackageEntity packageEntity = getPackageById(packageId);

        PackageEvaluationEntity packageEvaluation = getPackageEvaluationFromPackage(packageEntity, commentId);

        packageEntity.getPackageEvaluations().remove(packageEvaluation);

        packageEntity.getPackageEvaluations().remove(packageEvaluation);

        packageRepository.save(packageEntity);

        return CompletableFuture.completedFuture(ResponseEntity.ok("Comment deleted successfully"));
    }
}
