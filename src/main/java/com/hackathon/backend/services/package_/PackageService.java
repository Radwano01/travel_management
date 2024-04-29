package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.packageDto.EssentialPackageDto;
import com.hackathon.backend.dto.packageDto.PackageDetailsDto;
import com.hackathon.backend.dto.packageDto.PackageDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PackageService {

    private final PackageUtils packageUtils;
    private final PackageDetailsUtils packageDetailsUtils;
    private final CountryUtils countryUtils;

    @Autowired
    public PackageService(PackageUtils packageUtils,
                          PackageDetailsUtils packageDetailsUtils,
                          CountryUtils countryUtils){
        this.packageUtils = packageUtils;
        this.packageDetailsUtils = packageDetailsUtils;
        this.countryUtils = countryUtils;
    }

    public ResponseEntity<?> createPackage(int countryId,
                                           @NonNull PackageDto packageDto){
        try{
            CountryEntity country = countryUtils.findCountryById(countryId);

            PackageEntity packageEntity = new PackageEntity(
                    packageDto.getPackageName(),
                    packageDto.getPrice(),
                    packageDto.getMainImage(),
                    country
            );
            packageUtils.save(packageEntity);

            PackageDetailsDto packageDetailsDto = packageDto.getPackageDetails();
            PackageDetailsEntity packageDetails = new PackageDetailsEntity(
                    packageDetailsDto.getImageOne(),
                    packageDetailsDto.getImageTwo(),
                    packageDetailsDto.getImageThree(),
                    packageDetailsDto.getDescription(),
                    packageEntity
            );
            packageDetailsUtils.save(packageDetails);
            return ResponseEntity.ok("Package and package Details created successfully");
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getPackagesByCountry(int countryId){
        try{
            List<EssentialPackageDto> packages = packageUtils
                    .findPackagesByCountryId(countryId);
            return ResponseEntity.ok(packages);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editPackage(int packageId,
                                         PackageDto packageDto){
        try{
            PackageEntity packageEntity = packageUtils.findById(packageId);
            editHelper(packageEntity, packageDto);
            packageUtils.save(packageEntity);
            return ResponseEntity.ok("Package edited successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> deletePackage(int packageId){
        try{
            PackageEntity packageEntity = packageUtils.findById(packageId);
            PackageDetailsEntity packageDetails = packageDetailsUtils.findByPackageOfferId(packageId);
            if(packageEntity != null && packageDetails != null) {
                packageDetailsUtils.deleteByPackageOfferId(packageId);
                packageUtils.deleteById(packageId);
                return ResponseEntity.ok("Package deleted successfully");
            }else{
                return ResponseEntity.ok("Package not found");
            }
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    private void editHelper(PackageEntity packageEntity,
                            PackageDto packageDto) {
        if(packageDto.getPackageName() != null){
            packageEntity.setPackageName(packageDto.getPackageName());
        }
        if(packageDto.getMainImage() != null){
            packageEntity.setMainImage(packageEntity.getMainImage());
        }
        if(packageDto.getPrice() > 0){
            packageEntity.setPrice(packageEntity.getPrice());
        }
        if(packageDto.getRate() > 0){
            packageEntity.setRate(packageEntity.getRate());
        }
    }
}
