package com.hackathon.backend.repositories.country;

import com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceForFlightDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity,Integer> {
    @Query("SELECT new com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto(p.id,p.place,p.mainImage) " +
            "FROM PlaceEntity p WHERE p.country.id = :countryId")
    List<GetEssentialPlaceDto> findPlacesByCountryId(int countryId);

    @Query("SELECT new com.hackathon.backend.dto.countryDto.placeDto.GetPlaceForFlightDto(p.id, p.place, ap.airPortName) " +
            "FROM PlaceEntity p " +
            "JOIN p.airPorts ap " +
            "WHERE p.place = :place")
    List<GetPlaceForFlightDto> findPlaceByPlace(String place);
}
