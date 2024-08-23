package com.hackathon.backend.controllers.package_;

import com.hackathon.backend.dto.packageDto.EditPackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.CreatePackageEvaluationDto;
import com.hackathon.backend.services.package_.PackageEvaluationService;
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

    private final PackageEvaluationService packageEvaluationService;

    @Autowired
    public PackageEvaluationController(PackageEvaluationService packageEvaluationService) {
        this.packageEvaluationService = packageEvaluationService;
    }

    @PostMapping(path = "${ADD_PACKAGE_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> addComment(@PathVariable("packageId") int packageId,
                                                           @PathVariable("userId") long userId,
                                                           @RequestBody CreatePackageEvaluationDto createPackageEvaluationDto){
        try {
            return packageEvaluationService.addComment(packageId, userId, createPackageEvaluationDto);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }

    @GetMapping(path = "${GET_PACKAGE_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> getComments(@PathVariable("packageId") int packageId){
        try {
            return packageEvaluationService.getComments(packageId);
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
            return packageEvaluationService.editComment(packageId, commentId, editPackageEvaluationDto);
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
            return packageEvaluationService.removeComment(packageId, commentId);
        }catch (EntityNotFoundException e) {
            return CompletableFuture.completedFuture(notFoundException(e));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(serverErrorException(e));
        }
    }
}
