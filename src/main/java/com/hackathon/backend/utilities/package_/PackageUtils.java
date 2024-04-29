package com.hackathon.backend.utilities.package_;

import com.hackathon.backend.dto.packageDto.EssentialPackageDto;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.repositories.package_.PackageRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PackageUtils {

    private final PackageRepository packageRepository;

    @Autowired
    public PackageUtils(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    public void save(PackageEntity packageEntity) {
        packageRepository.save(packageEntity);
    }

    public List<EssentialPackageDto> findPackagesByCountryId(int countryId) {
        return packageRepository.findPackagesByCountryId(countryId);
    }

    public PackageEntity findById(int packageId) {
        return packageRepository.findById(packageId)
                .orElseThrow(()-> new EntityNotFoundException("package id not found"));
    }

    public void deleteById(int packageId) {
        packageRepository.deleteById(packageId);
    }
}
