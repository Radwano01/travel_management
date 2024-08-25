package com.hackathon.backend.controllers.package_;

import com.hackathon.backend.dto.packageDto.EditPackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.CreatePackageEvaluationDto;
import com.hackathon.backend.services.package_.impl.PackageEvaluationServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PackageEvaluationController {

    private final PackageEvaluationServiceImpl packageEvaluationServiceImpl;

    @Autowired
    public PackageEvaluationController(PackageEvaluationServiceImpl packageEvaluationServiceImpl) {
        this.packageEvaluationServiceImpl = packageEvaluationServiceImpl;
    }

    @PostMapping(path = "${ADD_PACKAGE_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> addComment(@PathVariable("packageId") int packageId,
                                                           @PathVariable("userId") long userId,
                                                           @RequestBody CreatePackageEvaluationDto createPackageEvaluationDto){
        try {
            return packageEvaluationServiceImpl.addComment(packageId, userId, createPackageEvaluationDto);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @GetMapping(path = "${GET_PACKAGE_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> getComments(@PathVariable("packageId") int packageId){
        try {
            return packageEvaluationServiceImpl.getComments(packageId);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @PutMapping(path = "${EDIT_PACKAGE_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> editComment(@PathVariable("packageId") int packageId,
                                                            @PathVariable("commentId") long commentId,
                                                            @RequestBody EditPackageEvaluationDto editPackageEvaluationDto){
        try {
            return packageEvaluationServiceImpl.editComment(packageId, commentId, editPackageEvaluationDto);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @DeleteMapping(path = "${REMOVE_PACKAGE_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> removeComment(@PathVariable("packageId") int packageId,
                                                              @PathVariable("commentId") long commentId){
        try {
            return packageEvaluationServiceImpl.removeComment(packageId, commentId);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }
}
