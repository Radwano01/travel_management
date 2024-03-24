package com.hackathon.backend.Repositories;

import com.hackathon.backend.Entities.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Integer> {
        boolean existsByHotelName(String hotelName);
}

