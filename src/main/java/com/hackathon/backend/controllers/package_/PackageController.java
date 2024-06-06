package com.hackathon.backend.controllers.package_;


import com.hackathon.backend.dto.packageDto.PackageDto;
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

        PostP p = new PostP(
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
                                         @RequestBody PackageDto packageDto){
        return packageService.editPackage(packageId,packageDto);
    }

    @DeleteMapping(path = "${DELETE_PACKAGE_PATH}")
    public ResponseEntity<?> deletePackage(@PathVariable("packageId") int packageId){
        return packageService.deletePackage(packageId);
    }
}
