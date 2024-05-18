package com.hackathon.backend.repositories.plane;


import com.hackathon.backend.entities.plane.PlaneSeatsBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PlaneSeatsBookingRepository extends JpaRepository<PlaneSeatsBookingEntity, Long> {
}
