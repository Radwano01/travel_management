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

    public List<PackageDetailsEntity> findAll() {
        return packageDetailsRepository.findAll();
    }

    public PackageDetailsEntity findById(int packageDetailsId) {
        return packageDetailsRepository.findById(packageDetailsId)
                .orElseThrow(()-> new EntityNotFoundException("Package id not found"));
    }

    public void delete(PackageDetailsEntity packageDetails) {
        packageDetailsRepository.delete(packageDetails);
    }
}
