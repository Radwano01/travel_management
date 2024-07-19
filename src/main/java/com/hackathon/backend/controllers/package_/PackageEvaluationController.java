package com.hackathon.backend.controllers.package_;

import com.hackathon.backend.dto.packageDto.EditPackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.PackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.PostPackageEvaluationDto;
import com.hackathon.backend.services.package_.PackageEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PackageEvaluationController {

    private final PackageEvaluationService packageEvaluationService;

    @Autowired
    public PackageEvaluationController(PackageEvaluationService packageEvaluationService) {
        this.packageEvaluationService = packageEvaluationService;
    }

    @PostMapping(path = "${ADD_PACKAGE_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<String>> addComment(@PathVariable("packageId") int packageId,
                                                                @PathVariable("userId") long userId,
                                                                @RequestBody PostPackageEvaluationDto postPackageEvaluationDto){
        return packageEvaluationService.addComment(packageId, userId, postPackageEvaluationDto);
    }

    @GetMapping(path = "${GET_PACKAGE_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<?>> getComments(@PathVariable("packageId") int packageId){
        return packageEvaluationService.getComments(packageId);
    }

    @PutMapping(path = "${EDIT_PACKAGE_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<String>> editComment(@PathVariable("commentId") long commentId,
                                              @RequestBody EditPackageEvaluationDto editPackageEvaluationDto){
        return packageEvaluationService.editComment(commentId, editPackageEvaluationDto);
    }

    @DeleteMapping(path = "${REMOVE_PACKAGE_EVALUATION_PATH}")
    public CompletableFuture<ResponseEntity<String>> removeComment(@PathVariable("commentId") long commentId){
        return packageEvaluationService.removeComment(commentId);
    }
}
