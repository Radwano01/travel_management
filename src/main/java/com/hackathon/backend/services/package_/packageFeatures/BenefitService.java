package com.hackathon.backend.services.package_.packageFeatures;


import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.utilities.package_.PackageDetailsUtils;
import com.hackathon.backend.utilities.package_.features.BenefitUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class BenefitService{

    private final BenefitUtils benefitUtils;
    private final PackageDetailsUtils packageDetailsUtils;

    @Autowired
    public BenefitService(BenefitUtils benefitUtils,
                          PackageDetailsUtils packageDetailsUtils){
        this.benefitUtils = benefitUtils;
        this.packageDetailsUtils = packageDetailsUtils;
    }

    public ResponseEntity<?> createBenefit(String benefit){
        try{
            if(benefitUtils.existsByBenefit(benefit)) {
                return alreadyValidException("Benefit already exists");
            }
            BenefitEntity benefitEntity = new BenefitEntity(
                benefit
            );
            benefitUtils.save(benefitEntity);
            return ResponseEntity.ok("Benefit created successfully");
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    public ResponseEntity<?> getBenefits(){
        try{
            List<BenefitEntity> benefits = benefitUtils.findAll();
            return ResponseEntity.ok(benefits);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> editBenefit(int benefitId, String benefit){
        try{
            BenefitEntity benefitEntity = benefitUtils.findById(benefitId);
            if(benefit != null){
                benefitEntity.setBenefit(benefit);
            }
            benefitUtils.save(benefitEntity);
            return ResponseEntity.ok("Benefit edit successfully");
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<?> deleteBenefit(int benefitId) {
        try {
            BenefitEntity benefit = benefitUtils.findById(benefitId);
            if (benefit != null) {
                List<PackageDetailsEntity> packageDetailsEntities = packageDetailsUtils.findAll();
                for (PackageDetailsEntity packageDetails : packageDetailsEntities) {
                    if (packageDetails.getBenefits() != null) {
                        List<BenefitEntity> benefitEntities = packageDetails.getBenefits();
                        boolean removeIf = benefitEntities.removeIf((b) -> b.getId() == benefitId);
                        if (removeIf) {
                            packageDetailsUtils.save(packageDetails);
                        }
                    } else {
                        return notFoundException("This package has no benefits");
                    }
                }
                benefitUtils.deleteById(benefitId);
                return ResponseEntity.ok("Benefit deleted successfully");
            } else {
                return notFoundException("Benefit not found");
            }
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }

}