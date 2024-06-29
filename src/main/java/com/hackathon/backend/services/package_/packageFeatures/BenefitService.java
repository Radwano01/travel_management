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

    public ResponseEntity<String> createBenefit(String benefit){
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
    public ResponseEntity<String> editBenefit(int benefitId,
                                         String benefit){
        try{
            if(benefit == null){
                return badRequestException("you sent an empty data to change");
            }
            BenefitEntity benefitEntity = benefitUtils.findById(benefitId);
            benefitEntity.setBenefit(benefit);
            benefitUtils.save(benefitEntity);
            return ResponseEntity.ok("Benefit edit successfully");
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @Transactional
    public ResponseEntity<String> deleteBenefit(int benefitId) {
        try {
            BenefitEntity benefitEntity = benefitUtils.findById(benefitId);
            for (PackageDetailsEntity packageDetails : benefitEntity.getPackageDetails()) {
                packageDetails.getBenefits().remove(benefitEntity);
                packageDetailsUtils.save(packageDetails);
            }
            benefitUtils.delete(benefitEntity);
            return ResponseEntity.ok("Benefit deleted successfully");
        } catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}