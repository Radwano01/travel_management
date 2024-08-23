package com.hackathon.backend.controllers.package_;

import com.hackathon.backend.dto.packageDto.CreatePackageDto;
import com.hackathon.backend.dto.packageDto.EditPackageDto;
import com.hackathon.backend.services.package_.PackageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class PackageController {

    private final PackageService packageService;

    @Autowired
    public PackageController(PackageService packageService){
        this.packageService = packageService;
    }

    @PostMapping(path = "${CREATE_PACKAGE_PATH}")
    public ResponseEntity<String> createPackage(@PathVariable("countryId") int countryId,
                                                @ModelAttribute CreatePackageDto createPackageDto) {
        try {
            return packageService.createPackage(countryId, createPackageDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_PACKAGE_PATH}")
    public ResponseEntity<?> getPackages(@PathVariable("countryId") int countryId){
        try {
            return packageService.getPackagesByCountry(countryId);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_PACKAGE_PATH}")
    public ResponseEntity<String> editPackage(@PathVariable("countryId") int countryId,
                                              @PathVariable("packageId") int packageId,
                                              @ModelAttribute EditPackageDto editPackageDto){
        try {
            return packageService.editPackage(countryId, packageId, editPackageDto);
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_PACKAGE_PATH}")
    public ResponseEntity<String> deletePackage(@PathVariable("countryId") int countryId,
                                                @PathVariable("packageId") int packageId){
        try {
            return packageService.deletePackage(countryId, packageId);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }
}
