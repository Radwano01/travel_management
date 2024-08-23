package com.hackathon.backend.repositories.hotel;

import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelEvaluationRepository extends JpaRepository<HotelEvaluationEntity, Long> {
    HotelEvaluationEntity findHotelEvaluationByUserId(long userId);
}
