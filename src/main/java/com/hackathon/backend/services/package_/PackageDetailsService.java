package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.packageDto.EditPackageDetailsDto;
import com.hackathon.backend.dto.packageDto.GetPackageANDPackageDetailsDto;
import org.springframework.http.ResponseEntity;

public interface PackageDetailsService {

    ResponseEntity<GetPackageANDPackageDetailsDto> getSinglePackageDetails(int packageId);

    ResponseEntity<String> editPackageDetails(int packageId, EditPackageDetailsDto editPackageDetailsDto);
}
