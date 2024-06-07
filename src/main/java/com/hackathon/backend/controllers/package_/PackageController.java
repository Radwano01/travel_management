package com.hackathon.backend.controllers.package_;


import com.hackathon.backend.dto.packageDto.EditPackageDto;
import com.hackathon.backend.dto.packageDto.GetPackageDto;
import com.hackathon.backend.dto.packageDto.PostPackageDto;
import com.hackathon.backend.services.package_.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PackageController {

    private final PackageService packageService;

    @Autowired
    public PackageController(PackageService packageService){
        this.packageService = packageService;
    }

    @PostMapping(path = "${CREATE_PACKAGE_PATH}")
    public ResponseEntity<?> createPackage(@PathVariable("countryId") int countryId,
                                           @RequestParam("packageName") String packageName,
                                           @RequestParam("price") float price,
                                           @RequestParam("rate") float rate,
                                           @RequestParam("mainImage") MultipartFile mainImage,
                                           @RequestParam("imageOne") MultipartFile imageOne,
                                           @RequestParam("imageTwo") MultipartFile imageTwo,
                                           @RequestParam("imageThree") MultipartFile imageThree,
                                           @RequestParam("description") String description) {

        PostPackageDto p = new PostPackageDto(
                packageName,
                price,
                rate,
                mainImage,
                imageOne,
                imageTwo,
                imageThree,
                description
        );
        return packageService.createPackage(countryId,p);
    }

    @GetMapping(path = "${GET_PACKAGE_PATH}")
    public ResponseEntity<?> getPackages(@PathVariable("countryId") int countryId){
        return packageService.getPackagesByCountry(countryId);
    }

    @PutMapping(path = "${EDIT_PACKAGE_PATH}")
    public ResponseEntity<?> editPackage(@PathVariable("packageId") int packageId,
                                         @RequestParam(name = "packageName",required = false) String packageName,
                                         @RequestParam(name = "price",required = false) float price,
                                         @RequestParam(name = "rate",required = false) float rate,
                                         @RequestParam(name = "mainImage",required = false) MultipartFile mainImage){
        EditPackageDto editPackageDto = new EditPackageDto(
                packageName,
                price,
                rate,
                mainImage
        );
        return packageService.editPackage(packageId, editPackageDto);
    }

    @DeleteMapping(path = "${DELETE_PACKAGE_PATH}")
    public ResponseEntity<?> deletePackage(@PathVariable("packageId") int packageId){
        return packageService.deletePackage(packageId);
    }
}
