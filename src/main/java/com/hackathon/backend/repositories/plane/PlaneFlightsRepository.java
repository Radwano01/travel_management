package com.hackathon.backend.repositories.plane;

import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaneFlightsRepository extends JpaRepository<PlaneFlightsEntity,Long> {
    List<PlaneFlightsEntity> findAllByDepartureCountryIdAndDestinationCountryId(int departureCountryId, int destinationCountryId);
}
