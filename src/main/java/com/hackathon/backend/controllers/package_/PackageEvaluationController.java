package com.hackathon.backend.controllers.package_;

import com.hackathon.backend.dto.packageDto.EditPackageEvaluationDto;
import com.hackathon.backend.dto.packageDto.PackageEvaluationDto;
import com.hackathon.backend.services.package_.PackageEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PackageEvaluationController {

    private final PackageEvaluationService packageEvaluationService;

    @Autowired
    public PackageEvaluationController(PackageEvaluationService packageEvaluationService) {
        this.packageEvaluationService = packageEvaluationService;
    }

    @PostMapping(path = "${ADD_PACKAGE_EVALUATION_PATH}")
    public ResponseEntity<String> addComment(@PathVariable("packageId") int packageId,
                                        @PathVariable("userId") long userId,
                                        @RequestBody PackageEvaluationDto packageEvaluationDto){
        return packageEvaluationService.addComment(packageId, userId, packageEvaluationDto);
    }

    @GetMapping(path = "${GET_PACKAGE_EVALUATION_PATH}")
    public ResponseEntity<?> getComments(@PathVariable("packageId") int packageId){
        return packageEvaluationService.getComments(packageId);
    }

    @PutMapping(path = "${EDIT_PACKAGE_EVALUATION_PATH}")
    public ResponseEntity<String> editComment(@PathVariable("commentId") long commentId,
                                              @RequestBody EditPackageEvaluationDto editPackageEvaluationDto){
        return packageEvaluationService.editComment(commentId, editPackageEvaluationDto);
    }

    @DeleteMapping(path = "${REMOVE_PACKAGE_EVALUATION_PATH}")
    public ResponseEntity<String> removeComment(@PathVariable("commentId") long commentId){
        return packageEvaluationService.removeComment(commentId);
    }
}
