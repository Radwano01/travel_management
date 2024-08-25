package com.hackathon.backend.services.package_.packageFeatures.impl;


import com.hackathon.backend.dto.packageDto.features.CreateBenefitDto;
import com.hackathon.backend.dto.packageDto.features.EditBenefitDto;
import com.hackathon.backend.dto.packageDto.features.GetBenefitDto;
import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.repositories.package_.PackageDetailsRepository;
import com.hackathon.backend.repositories.package_.packageFeatures.BenefitRepository;
import com.hackathon.backend.services.package_.packageFeatures.BenefitService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hackathon.backend.libs.MyLib.checkIfSentEmptyData;
import static com.hackathon.backend.utilities.ErrorUtils.*;

@Service
public class BenefitServiceImpl implements BenefitService {

    private final BenefitRepository benefitRepository;
    private final PackageDetailsRepository packageDetailsRepository;

    @Autowired
    public BenefitServiceImpl(BenefitRepository benefitRepository,
                              PackageDetailsRepository packageDetailsRepository){
        this.benefitRepository = benefitRepository;
        this.packageDetailsRepository = packageDetailsRepository;
    }

    @Override
    public ResponseEntity<String> createBenefit(CreateBenefitDto createBenefitDto){
        String benefit = createBenefitDto.getBenefit().trim();

        ResponseEntity<String> checkResult = checkIfBenefitAlreadyExist(benefit);
        if(!checkResult.getStatusCode().equals(HttpStatus.OK)){
            return checkResult;
        }

        benefitRepository.save(new BenefitEntity(benefit));

        return ResponseEntity.ok("Benefit created successfully" + benefit);
    }

    private ResponseEntity<String> checkIfBenefitAlreadyExist(String benefit){
        boolean existsBenefit = benefitRepository.existsBenefitByBenefit(benefit);

        if(existsBenefit){
            return alreadyValidException("Hotel Feature already exists");
        }
        return ResponseEntity.ok("OK");
    }

    @Override
    public ResponseEntity<List<GetBenefitDto>> getBenefits(){
        return ResponseEntity.ok(benefitRepository.findAllBenefits());
    }

    @Transactional
    @Override
    public ResponseEntity<String> editBenefit(int benefitId, EditBenefitDto editBenefitDto){
        if(!checkIfSentEmptyData(editBenefitDto)){
            return badRequestException("you sent an empty data to change");
        }

        String benefit = editBenefitDto.getBenefit().trim();

        BenefitEntity benefitEntity = findBenefitById(benefitId);

        benefitEntity.setBenefit(benefit);

        benefitRepository.save(benefitEntity);

        return ResponseEntity.ok("Benefit edit successfully" + benefit);
    }

    private BenefitEntity findBenefitById(int benefitId) {
        return benefitRepository.findById(benefitId)
                .orElseThrow(()-> new EntityNotFoundException("No such benefit has this id"));
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteBenefit(int benefitId) {
        BenefitEntity benefitEntity = findBenefitById(benefitId);

        for (PackageDetailsEntity packageDetails : benefitEntity.getPackageDetails()) {
            packageDetails.getBenefits().remove(benefitEntity);
            packageDetailsRepository.save(packageDetails);
        }

        benefitRepository.delete(benefitEntity);

        return ResponseEntity.ok("Benefit deleted successfully");
    }
}