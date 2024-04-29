package com.hackathon.backend.repositories.country;

import com.hackathon.backend.entities.country.PlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity,Integer> {
    Optional<PlaceEntity> findByPlace(String newPlace);

    List<PlaceEntity> findAllPlacesByCountryId(int countryId);
}
