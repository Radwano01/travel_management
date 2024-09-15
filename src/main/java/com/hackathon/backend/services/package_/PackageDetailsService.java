package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.packageDto.EditPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.GetPackageANDPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.features.GetRoadmapDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PackageDetailsService {

    ResponseEntity<GetPackageANDPackageDetailsDto> getSinglePackageDetails(int packageId);

    ResponseEntity<List<GetRoadmapDto>> getRoadmapsFromPackage(int packageId);

    ResponseEntity<?> getBenefitsFromPackage(int packageId);

    ResponseEntity<String> editPackageDetails(int packageId, EditPackageDetailsDto editPackageDetailsDto);

}
