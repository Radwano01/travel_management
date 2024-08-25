package com.hackathon.backend.services.package_.packageFeatures;

import com.hackathon.backend.dto.packageDto.features.CreateBenefitDto;
import com.hackathon.backend.dto.packageDto.features.EditBenefitDto;
import com.hackathon.backend.dto.packageDto.features.GetBenefitDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BenefitService {

    ResponseEntity<String> createBenefit(CreateBenefitDto createBenefitDto);

    ResponseEntity<List<GetBenefitDto>> getBenefits();

    ResponseEntity<String> editBenefit(int benefitId, EditBenefitDto editBenefitDto);

    ResponseEntity<String> deleteBenefit(int benefitId);
}
