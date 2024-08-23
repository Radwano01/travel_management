package com.hackathon.backend.repositories.plane;

import com.hackathon.backend.dto.planeDto.GetAirPortDto;
import com.hackathon.backend.entities.plane.AirPortEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirPortRepository extends JpaRepository<AirPortEntity, Long> {
}
