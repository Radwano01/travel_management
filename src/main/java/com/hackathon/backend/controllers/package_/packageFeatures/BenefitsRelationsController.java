package com.hackathon.backend.controllers.package_.packageFeatures;

import com.hackathon.backend.services.package_.packageFeatures.PackageBenefitsRelationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class BenefitsRelationsController {

    private final PackageBenefitsRelationsService packageBenefitsRelationsService;

    @Autowired
    public BenefitsRelationsController(PackageBenefitsRelationsService packageBenefitsRelationsService) {
        this.packageBenefitsRelationsService = packageBenefitsRelationsService;
    }


    @PostMapping(path = "${PACKAGE_BENEFIT_RELATIONS_PATH}")
    public ResponseEntity<?> addPackageRoadmap(@PathVariable("packageId") int packageId,
                                               @PathVariable("benefitId") int benefitId){
        return packageBenefitsRelationsService.addPackageBenefit(packageId,benefitId);
    }

    @DeleteMapping(path = "${PACKAGE_BENEFIT_RELATIONS_PATH}")
    public ResponseEntity<?> removePackageRoadmap(@PathVariable("packageId") int packageId,
                                                  @PathVariable("benefitId") int benefitId){
        return packageBenefitsRelationsService.removePackageBenefit(packageId,benefitId);
    }
}
