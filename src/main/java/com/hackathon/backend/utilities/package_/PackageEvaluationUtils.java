package com.hackathon.backend.utilities.package_;

import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import com.hackathon.backend.repositories.package_.PackageEvaluationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PackageEvaluationUtils {

    private final PackageEvaluationRepository packageEvaluationRepository;

    @Autowired
    public PackageEvaluationUtils(PackageEvaluationRepository packageEvaluationRepository) {
        this.packageEvaluationRepository = packageEvaluationRepository;
    }

    public PackageEvaluationEntity findById(long commentId) {
        return packageEvaluationRepository.findById(commentId)
                .orElseThrow(()-> new EntityNotFoundException("Comment id not found"));
    }

    public void save(PackageEvaluationEntity packageEvaluation) {
        packageEvaluationRepository.save(packageEvaluation);
    }

    public void delete(PackageEvaluationEntity packageEvaluation) {
        packageEvaluationRepository.delete(packageEvaluation);
    }
}
