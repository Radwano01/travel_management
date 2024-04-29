package com.hackathon.backend.controllers.package_;


import com.hackathon.backend.dto.packageDto.PackageDto;
import com.hackathon.backend.services.package_.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                                           @RequestBody PackageDto packageDto){
        return packageService.createPackage(countryId,packageDto);
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
