package com.hackathon.backend.services.package_.packageFeatures;

import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.BenefitRepository;
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

    private final PackageRepository packageRepository;
    private final BenefitRepository benefitRepository;

    @Autowired
    public PackageBenefitsRelationsService(PackageRepository packageRepository,
                                           BenefitRepository benefitRepository) {
        this.packageRepository = packageRepository;
        this.benefitRepository = benefitRepository;
    }

    @Transactional
    public ResponseEntity<?> addPackageBenefit(int packageId, int benefitId) {
        try{
            PackageEntity packageEntity = packageRepository.findById(packageId)
                    .orElseThrow(()-> new EntityNotFoundException("Package id not found"));
            BenefitEntity benefitEntity = benefitRepository.findById(benefitId)
                    .orElseThrow(()-> new EntityNotFoundException("benefit id not found"));
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
            Optional<PackageEntity> packageEntity = packageRepository.findById(packageId);
            Optional<BenefitEntity> benefitEntity = benefitRepository.findById(benefitId);
            if(packageEntity.isPresent() && benefitEntity.isPresent()) {
                Optional<BenefitEntity> benefitEntityOptional = packageEntity.get().getPackageDetails().getBenefits().stream()
                        .filter((benefit) -> benefit.getId() == benefitId)
                        .findFirst();
                if(benefitEntityOptional.isPresent()) {
                    packageEntity.get().getPackageDetails().getBenefits().remove(benefitEntityOptional.get());
                    packageRepository.save(packageEntity.get());
                    benefitRepository.save(benefitEntity.get());
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
