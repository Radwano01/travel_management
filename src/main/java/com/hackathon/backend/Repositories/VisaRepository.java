package com.hackathon.backend.Repositories;

import com.hackathon.backend.Entities.VisaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VisaRepository extends JpaRepository<VisaEntity, Integer> {
    List<VisaEntity> findByUserId(int userId);
}
