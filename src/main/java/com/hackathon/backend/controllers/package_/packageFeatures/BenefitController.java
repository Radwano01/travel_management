package com.hackathon.backend.controllers.package_.packageFeatures;

import com.hackathon.backend.services.package_.packageFeatures.BenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${BASE_API}")
public class BenefitController{

    private final BenefitService benefitService;

    @Autowired
    public BenefitController(BenefitService benefitService){
        this.benefitService = benefitService;
    }

    @PostMapping(path = "${CREATE_BENEFIT_PATH}")
    public ResponseEntity<String> createBenefit(String benefit){
        return benefitService.createBenefit(benefit);
    }

    @GetMapping(path = "${GET_BENEFITS_PATH}")
    public ResponseEntity<?> getBenefits(){
        return benefitService.getBenefits();
    }

    @PutMapping(path = "${EDIT_BENEFIT_PATH}")
    public ResponseEntity<String> editBenefit(@PathVariable("benefitId") int benefitId,
                                         @RequestBody String benefit){
        return benefitService.editBenefit(benefitId, benefit);
    }

    @DeleteMapping(path = "${DELETE_BENEFIT_PATH}")
    public ResponseEntity<String> deleteBenefit(@PathVariable("benefitId") int benefitId){
        return benefitService.deleteBenefit(benefitId);
    }
}