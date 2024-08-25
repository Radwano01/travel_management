package com.hackathon.backend.controllers.package_.packageFeatures;

import com.hackathon.backend.dto.packageDto.features.CreateBenefitDto;
import com.hackathon.backend.dto.packageDto.features.EditBenefitDto;
import com.hackathon.backend.services.package_.packageFeatures.impl.BenefitServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.hackathon.backend.utilities.ErrorUtils.notFoundException;
import static com.hackathon.backend.utilities.ErrorUtils.serverErrorException;

@RestController
@RequestMapping(path = "${BASE_API}")
public class BenefitController{

    private final BenefitServiceImpl benefitServiceImpl;

    @Autowired
    public BenefitController(BenefitServiceImpl benefitServiceImpl){
        this.benefitServiceImpl = benefitServiceImpl;
    }

    @PostMapping(path = "${CREATE_BENEFIT_PATH}")
    public ResponseEntity<String> createBenefit(@RequestBody CreateBenefitDto createBenefitDto){
        try {
            return benefitServiceImpl.createBenefit(createBenefitDto);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @GetMapping(path = "${GET_BENEFITS_PATH}")
    public ResponseEntity<?> getBenefits(){
        try {
            return benefitServiceImpl.getBenefits();
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @PutMapping(path = "${EDIT_BENEFIT_PATH}")
    public ResponseEntity<String> editBenefit(@PathVariable("benefitId") int benefitId,
                                              @RequestBody EditBenefitDto editBenefitDto){
        try {
            return benefitServiceImpl.editBenefit(benefitId, editBenefitDto);
        }catch(EntityNotFoundException e){
            return notFoundException(e);
        }catch(Exception e){
            return serverErrorException(e);
        }
    }

    @DeleteMapping(path = "${DELETE_BENEFIT_PATH}")
    public ResponseEntity<String> deleteBenefit(@PathVariable("benefitId") int benefitId){
        try {
            return benefitServiceImpl.deleteBenefit(benefitId);
        }catch (EntityNotFoundException e) {
            return notFoundException(e);
        } catch (Exception e) {
            return serverErrorException(e);
        }
    }
}