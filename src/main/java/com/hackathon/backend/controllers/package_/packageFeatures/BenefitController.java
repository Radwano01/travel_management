package com.hackathon.backend.controllers.package_.packageFeatures;

import com.hackathon.backend.dto.packageDto.features.CreateBenefitDto;
import com.hackathon.backend.dto.packageDto.features.EditBenefitDto;
import com.hackathon.backend.services.package_.packageFeatures.BenefitService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class BenefitController{

    private final BenefitService benefitService;

    @Autowired
    public BenefitController(BenefitService benefitService){
        this.benefitService = benefitService;
    }

    @PostMapping(path = "${CREATE_BENEFIT_PATH}")
    public ResponseEntity<String> createBenefit(@RequestBody CreateBenefitDto createBenefitDto){
        try {
            return benefitService.createBenefit(createBenefitDto);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_BENEFITS_PATH}")
    public ResponseEntity<?> getBenefits(){
        try {
            return benefitService.getBenefits();
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_BENEFIT_PATH}")
    public ResponseEntity<String> editBenefit(@PathVariable("benefitId") int benefitId,
                                              @RequestBody EditBenefitDto editBenefitDto){
        try {
            return benefitService.editBenefit(benefitId, editBenefitDto);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_BENEFIT_PATH}")
    public ResponseEntity<String> deleteBenefit(@PathVariable("benefitId") int benefitId){
        try {
            return benefitService.deleteBenefit(benefitId);
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}