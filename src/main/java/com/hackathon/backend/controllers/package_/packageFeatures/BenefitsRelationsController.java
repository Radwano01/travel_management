package com.hackathon.backend.controllers.package_.packageFeatures;

import com.hackathon.backend.services.package_.packageFeatures.impl.PackageBenefitsRelationsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class BenefitsRelationsController {

    private final PackageBenefitsRelationsServiceImpl packageBenefitsRelationsServiceImpl;

    @Autowired
    public BenefitsRelationsController(PackageBenefitsRelationsServiceImpl packageBenefitsRelationsServiceImpl) {
        this.packageBenefitsRelationsServiceImpl = packageBenefitsRelationsServiceImpl;
    }


    @PostMapping(path = "${PACKAGE_BENEFIT_RELATIONS_PATH}")
    public ResponseEntity<String> addPackageRoadmap(@PathVariable("packageId") int packageId,
                                                    @PathVariable("benefitId") int benefitId){
        try {
            return packageBenefitsRelationsServiceImpl.addPackageBenefit(packageId, benefitId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${PACKAGE_BENEFIT_RELATIONS_PATH}")
    public ResponseEntity<String> removePackageRoadmap(@PathVariable("packageId") int packageId,
                                                       @PathVariable("benefitId") int benefitId){
        try {
            return packageBenefitsRelationsServiceImpl.removePackageBenefit(packageId, benefitId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
