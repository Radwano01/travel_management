package com.hackathon.backend.services.package_;

import com.hackathon.backend.dto.packageDto.CreatePackageDto;
import com.hackathon.backend.dto.packageDto.EditPackageDto;
import com.hackathon.backend.dto.packageDto.GetEssentialPackageDto;
import io.micrometer.common.lang.NonNull;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PackageService {
    ResponseEntity<String> createPackage(int countryId, @NonNull CreatePackageDto createPackageDto);

    ResponseEntity<List<GetEssentialPackageDto>> getPackagesByCountry(int countryId);

    ResponseEntity<String> editPackage(int countryId, int packageId, EditPackageDto editPackageDto);

    ResponseEntity<String> deletePackage(int countryId, int packageId);
}
