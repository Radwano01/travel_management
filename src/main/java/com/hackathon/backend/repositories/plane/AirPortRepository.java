package com.hackathon.backend.repositories.plane;

import com.hackathon.backend.entities.plane.AirPortEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirPortRepository extends JpaRepository<AirPortEntity, Long> {
    Optional<AirPortEntity> findAirPortByAirPortName(String airport);

    boolean existsAirPortByAirPortName(String airPortName);

    void deleteById(long airPortId);
}
