package com.hackathon.backend.utilities.package_.features;

import com.hackathon.backend.entities.package_.packageFeatures.BenefitEntity;
import com.hackathon.backend.repositories.package_.packageFeatures.BenefitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BenefitUtils {

    private final BenefitRepository benefitRepository;

    @Autowired
    public BenefitUtils(BenefitRepository benefitRepository) {
        this.benefitRepository = benefitRepository;
    }

    public boolean existsByBenefit(String benefit) {
        return benefitRepository.existsByBenefit(benefit);
    }

    public void save(BenefitEntity benefitEntity) {
        benefitRepository.save(benefitEntity);
    }

    public List<BenefitEntity> findAll() {
        return benefitRepository.findAll();
    }

    public BenefitEntity findById(int benefitId) {
        return benefitRepository.findById(benefitId)
                .orElseThrow(()-> new EntityNotFoundException("Benefit id not found"));
    }

    public void deleteById(int benefitId) {
        benefitRepository.deleteById(benefitId);
    }

    public void delete(BenefitEntity benefit) {
        benefitRepository.delete(benefit);
    }
}
