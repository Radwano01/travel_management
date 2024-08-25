package com.hackathon.backend.services.package_.packageFeatures;

import org.springframework.http.ResponseEntity;

public interface PackageRoadmapsRelationsService {

    ResponseEntity<String> addPackageRoadmap(int packageId, int roadmapId);

    ResponseEntity<String> removePackageRoadmap(int packageId, int roadmapId);
}
