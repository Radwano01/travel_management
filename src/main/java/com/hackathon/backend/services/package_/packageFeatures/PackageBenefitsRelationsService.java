package com.hackathon.backend.services.package_.packageFeatures;

import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.BenefitRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hackathon.backend.utilities.ErrorUtils.*;

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
    public ResponseEntity<String> addPackageBenefit(int packageId, int benefitId) {
        PackageEntity packageEntity = getPackageById(packageId);

        BenefitEntity benefitEntity = findBenefitById(benefitId);

        if(checkIfBenefitAlreadyExist(packageEntity, benefitId) != null){
            return alreadyValidException("This benefit is already existed");
        }

        packageEntity.getPackageDetails().getBenefits().add(benefitEntity);

        packageRepository.save(packageEntity);

        return ResponseEntity.ok("Benefit added successfully");
    }

    private PackageEntity getPackageById(int packageId){
        return packageRepository.findById(packageId)
                .orElseThrow(()-> new EntityNotFoundException("No such package has this id"));
    }

    private BenefitEntity checkIfBenefitAlreadyExist(PackageEntity packageEntity, int benefitId) {
        Optional<BenefitEntity> exist = packageEntity.getPackageDetails()
                .getBenefits().stream()
                .filter((benefit)-> benefit.getId() == benefitId)
                .findFirst();
        return exist.orElse(null);
    }

    private BenefitEntity findBenefitById(int benefitId){
        return benefitRepository.findById(benefitId)
                .orElseThrow(()-> new EntityNotFoundException("No such benefit has this id"));
    }

    @Transactional
    public ResponseEntity<String> removePackageBenefit(int packageId, int benefitId) {
        PackageEntity packageEntity = getPackageById(packageId);

        if(checkIfBenefitAlreadyExist(packageEntity, benefitId) == null){
            notFoundException("This benefit is not found");
        }

        BenefitEntity benefitEntity = findBenefitById(benefitId);

        packageEntity.getPackageDetails().getBenefits().remove(benefitEntity);

        packageRepository.save(packageEntity);

        return ResponseEntity.ok("Benefit removed from this package");
    }

}
