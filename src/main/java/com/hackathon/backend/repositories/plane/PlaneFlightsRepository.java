package com.hackathon.backend.repositories.plane;

import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaneFlightsRepository extends JpaRepository<PlaneFlightsEntity,Long> {

    @Query("SELECT f FROM PlaneFlightsEntity f WHERE f.departureAirPort.place.id = :departurePlaceId AND f.destinationAirPort.place.id = :destinationPlaceId")
    List<PlaneFlightsEntity> findAllByDeparturePlaceIdAndDestinationPlaceId(int departurePlaceId, int destinationPlaceId);
}
