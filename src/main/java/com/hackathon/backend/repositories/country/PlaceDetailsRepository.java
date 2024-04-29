package com.hackathon.backend.repositories.country;

import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceDetailsRepository extends JpaRepository<PlaceDetailsEntity, Integer> {
}
