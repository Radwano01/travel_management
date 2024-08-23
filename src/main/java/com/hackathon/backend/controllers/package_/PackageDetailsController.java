package com.hackathon.backend.controllers.package_;


import com.hackathon.backend.dto.packageDto.EditPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.GetPackageDetailsDto;
import com.hackathon.backend.services.package_.PackageDetailsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PackageDetailsController {

    private final PackageDetailsService packageDetailsService;

    @Autowired
    public PackageDetailsController(PackageDetailsService packageDetailsService) {
        this.packageDetailsService = packageDetailsService;
    }

    @GetMapping(path = "${GET_PACKAGE_DETAILS_PATH}")
    public ResponseEntity<?> getPackageDetails(@PathVariable("packageId") int packageId){
        try {
            return packageDetailsService.getSinglePackageDetails(packageId);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_PACKAGE_DETAILS_PATH}")
    public ResponseEntity<?> editPackageDetails(@PathVariable("packageId") int packageId,
                                                @ModelAttribute EditPackageDetailsDto editPackageDetailsDto){
        try {
            return packageDetailsService.editPackageDetails(packageId, editPackageDetailsDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }
}
