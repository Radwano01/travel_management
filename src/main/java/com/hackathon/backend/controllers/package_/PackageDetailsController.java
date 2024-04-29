package com.hackathon.backend.controllers.package_;


import com.hackathon.backend.dto.packageDto.PackageDetailsDto;
import com.hackathon.backend.services.package_.PackageDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return packageDetailsService.getSinglePackageDetails(packageId);
    }

    @PutMapping(path = "${EDIT_PACKAGE_DETAILS_PATH}")
    public ResponseEntity<?> editPackageDetails(@PathVariable("packageDetailsId") int packageDetailsId,
                                                @RequestBody PackageDetailsDto packageDetailsDto){
        return packageDetailsService.editPackageDetails(packageDetailsId, packageDetailsDto);
    }
}
