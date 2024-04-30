package com.hackathon.backend.utilities.package_;


import com.hackathon.backend.entities.package_.PackageDetailsEntity;
import com.hackathon.backend.repositories.package_.PackageDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PackageDetailsUtils {

    private final PackageDetailsRepository packageDetailsRepository;

    @Autowired
    public PackageDetailsUtils(PackageDetailsRepository packageDetailsRepository) {
        this.packageDetailsRepository = packageDetailsRepository;
    }

    public void save(PackageDetailsEntity packageDetails) {
        packageDetailsRepository.save(packageDetails);
    }

    public PackageDetailsEntity findByPackageOfferId(int packageId) {
        return packageDetailsRepository.findById(packageId)
                .orElseThrow(()-> new EntityNotFoundException("Package id not found"));
    }

    public void deleteByPackageOfferId(int packageId) {
        packageDetailsRepository.deleteByPackageOfferId(packageId);
    }

    public List<PackageDetailsEntity> findAll() {
        return packageDetailsRepository.findAll();
    }
}