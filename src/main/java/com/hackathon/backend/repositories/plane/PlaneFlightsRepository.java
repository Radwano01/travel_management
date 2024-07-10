package com.hackathon.backend.repositories.plane;

import com.hackathon.backend.dto.planeDto.FlightDto;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaneFlightsRepository extends JpaRepository<PlaneFlightsEntity, Long> {

    @Query("SELECT NEW com.hackathon.backend.dto.planeDto.FlightDto(" +
            "f.id, " +
            "f.plane.planeCompanyName, " +
            "f.price, " +
            "f.departureAirPort.airPortName, " +
            "f.departureAirPort.airPortCode, " +
            "f.destinationAirPort.airPortName, " +
            "f.destinationAirPort.airPortCode, " +
            "f.departureTime, " +
            "f.arrivalTime," +
            "f.availableSeats) " +
            "FROM PlaneFlightsEntity f " +
            "WHERE f.departureAirPort.id = :departureAirPortId " +
            "AND f.destinationAirPort.id = :destinationAirPortId")
    List<FlightDto> findAllByDepartureAirPortIdAndDestinationAirPortId(long departureAirPortId, long destinationAirPortId);
}
