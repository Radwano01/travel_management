package com.hackathon.backend.services.package_.packageFeatures;

import org.springframework.http.ResponseEntity;

public interface PackageBenefitsRelationsService {

    ResponseEntity<String> addPackageBenefit(int packageId, int benefitId);

    ResponseEntity<String> removePackageBenefit(int packageId, int benefitId);
}
