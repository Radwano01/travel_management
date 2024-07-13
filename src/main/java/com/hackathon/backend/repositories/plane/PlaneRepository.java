package com.hackathon.backend.repositories.plane;


import com.hackathon.backend.dto.planeDto.GetPlaneDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaneRepository extends JpaRepository<PlaneEntity, Long> {

    @Query("SELECT new com.hackathon.backend.dto.planeDto.GetPlaneDto(p.id, p.planeCompanyName, p.status) FROM PlaneEntity p")
    List<GetPlaneDto> findAllPlanes();
}
