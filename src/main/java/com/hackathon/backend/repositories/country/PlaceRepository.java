package com.hackathon.backend.repositories.country;

import com.hackathon.backend.dto.countryDto.placeDto.EssentialPlaceDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity,Integer> {


    @Query("SELECT new com.hackathon.backend.dto.countryDto.placeDto.EssentialPlaceDto(p.id,p.place,p.mainImage) " +
            "FROM PlaceEntity p WHERE p.country.id = :countryId")
    List<EssentialPlaceDto> findPlacesByCountryId(int countryId);
}
