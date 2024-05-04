package com.hackathon.backend.services.package_.packageFeatures;

import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.BenefitRepository;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.hackathon.backend.utilities.package_.features.BenefitUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@Service
public class PackageBenefitsRelationsService {

    private final PackageUtils packageUtils;
    private final BenefitUtils benefitUtils;

    @Autowired
    public PackageBenefitsRelationsService(PackageUtils packageUtils,
                                           BenefitUtils benefitUtils) {
        this.packageUtils = packageUtils;
        this.benefitUtils = benefitUtils;
    }

    @Transactional
    public ResponseEntity<?> addPackageBenefit(int packageId, int benefitId) {
        try{
            PackageEntity packageEntity = packageUtils.findById(packageId);
                    BenefitEntity benefitEntity = benefitUtils.findById(benefitId);
            Optional<BenefitEntity> benefitEntityOptional = packageEntity.getPackageDetails()
                    .getBenefits().stream()
                    .filter((benefit)-> benefit.getId() == benefitId)
                    .findFirst();
            if(benefitEntityOptional.isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("This benefit is already valid for this package");
            }
            packageEntity.getPackageDetails().getBenefits().add(benefitEntity);
            return ResponseEntity.ok("Benefit added successfully");
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> removePackageBenefit(int packageId, int benefitId) {
        try{
            PackageEntity packageEntity = packageUtils.findById(packageId);
            BenefitEntity benefitEntity = benefitUtils.findById(benefitId);
            if(packageEntity != null && benefitEntity != null) {
                Optional<BenefitEntity> benefitEntityOptional = packageEntity.getPackageDetails().getBenefits().stream()
                        .filter((benefit) -> benefit.getId() == benefitId)
                        .findFirst();
                if(benefitEntityOptional.isPresent()) {
                    packageEntity.getPackageDetails().getBenefits().remove(benefitEntityOptional.get());
                    packageUtils.save(packageEntity);
                    benefitUtils.save(benefitEntity);
                    return ResponseEntity.ok("Benefit removed from this package");
                }else{
                    return notFoundException("Benefit not found in this package");
                }
            }else{
                return notFoundException("Package or benefit not found");
            }
        }catch (EntityNotFoundException e){
            return notFoundException(e);
        }catch (Exception e){
            return serverErrorException(e);
        }
    }

}
