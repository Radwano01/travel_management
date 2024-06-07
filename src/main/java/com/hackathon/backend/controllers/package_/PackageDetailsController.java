package com.hackathon.backend.controllers.package_;


import com.hackathon.backend.dto.packageDto.EditPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.GetPackageDetailsDto;
import com.hackathon.backend.services.package_.PackageDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                                                @RequestParam(name = "imageOne", required = false) MultipartFile imageOne,
                                                @RequestParam(name = "imageTwo", required = false) MultipartFile imageTwo,
                                                @RequestParam(name = "imageThree", required = false) MultipartFile imageThree,
                                                @RequestParam(name = "description", required = false) String description){
        EditPackageDetailsDto editPackageDetailsDto = new EditPackageDetailsDto(
                imageOne,
                imageTwo,
                imageThree,
                description
        );
        return packageDetailsService.editPackageDetails(packageDetailsId, editPackageDetailsDto);
    }
}
