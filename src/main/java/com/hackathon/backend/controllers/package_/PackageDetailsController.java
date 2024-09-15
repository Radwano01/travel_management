package com.hackathon.backend.controllers.package_;


import com.hackathon.backend.dto.packageDto.EditPackageDetailsDto;
import com.hackathon.backend.services.package_.impl.PackageDetailsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PackageDetailsController {

    private final PackageDetailsServiceImpl packageDetailsServiceImpl;

    @Autowired
    public PackageDetailsController(PackageDetailsServiceImpl packageDetailsServiceImpl) {
        this.packageDetailsServiceImpl = packageDetailsServiceImpl;
    }

    @GetMapping(path = "${GET_PACKAGE_DETAILS_PATH}")
    public ResponseEntity<?> getPackageDetails(@PathVariable("packageId") int packageId){
        try {
            return packageDetailsServiceImpl.getSinglePackageDetails(packageId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_ROADMAPS_FROM_PACKAGE_PATH}")
    public ResponseEntity<?> getRoadmapsFromPackage(@PathVariable("packageId") int packageId){
        try{
            return packageDetailsServiceImpl.getRoadmapsFromPackage(packageId);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_BENEFITS_FROM_PACKAGE_PATH}")
    public ResponseEntity<?> getBenefitsFromPackage(@PathVariable("packageId") int packageId){
        try{
            return packageDetailsServiceImpl.getBenefitsFromPackage(packageId);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_PACKAGE_DETAILS_PATH}")
    public ResponseEntity<?> editPackageDetails(@PathVariable("packageId") int packageId,
                                                @ModelAttribute EditPackageDetailsDto editPackageDetailsDto){
        try {
            return packageDetailsServiceImpl.editPackageDetails(packageId, editPackageDetailsDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
