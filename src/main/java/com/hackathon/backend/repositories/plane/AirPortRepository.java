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
    Optional<AirPortEntity> findAirPortByAirPortName(String airport);

    boolean existsAirPortByAirPortName(String airPortName);

    void deleteById(long airPortId);

    boolean existsAirPortByAirPortCode(String airPortCode);

    @Query("SELECT new com.hackathon.backend.dto.planeDto.GetAirPortDto" +
            "(a.airPortName, a.airPortCode) FROM AirPortEntity a WHERE a.place.id = :placeId")
    Optional<List<GetAirPortDto>> findByPlaceId(int placeId);
}
