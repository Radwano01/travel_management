package com.hackathon.backend.controllers.package_;

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
    public ResponseEntity<?> addComment(@PathVariable("packageId") int packageId,
                                        @PathVariable("userId") long userId,
                                        @RequestBody PackageEvaluationDto packageEvaluationDto){
        return packageEvaluationService.addComment(packageId, userId, packageEvaluationDto);
    }

    @GetMapping(path = "${GET_PACKAGE_EVALUATION_PATH}")
    public ResponseEntity<?> getComments(@PathVariable("packageId") int packageId){
        return packageEvaluationService.getComments(packageId);
    }

    @PutMapping(path = "${EDIT_PACKAGE_EVALUATION_PATH}")
    public ResponseEntity<?> editComment(@PathVariable("commentId") long commentId,
                                         @RequestBody PackageEvaluationDto packageEvaluationDto){
        return packageEvaluationService.editComment(commentId, packageEvaluationDto);
    }

    @DeleteMapping(path = "${REMOVE_PACKAGE_EVALUATION_PATH}")
    public ResponseEntity<?> removeComment(@PathVariable("packageId") int packageId,
                                           @PathVariable("userId") long userId,
                                           @PathVariable("commentId") long commentId){
        return packageEvaluationService.removeComment(packageId, userId, commentId);
    }
}
