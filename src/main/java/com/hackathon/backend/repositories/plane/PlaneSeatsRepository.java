package com.hackathon.backend.repositories.plane;


import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaneSeatsRepository extends JpaRepository<PlaneSeatsEntity, Long> {
    Optional<List<PlaneSeatsEntity>> findAllSeatsByPlaneId(long planeId);
}
