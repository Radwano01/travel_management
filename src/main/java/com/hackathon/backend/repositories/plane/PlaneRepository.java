package com.hackathon.backend.repositories.plane;


import com.hackathon.backend.entities.plane.PlaneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaneRepository extends JpaRepository<PlaneEntity, Long> {
}
