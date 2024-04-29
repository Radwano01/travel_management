package com.hackathon.backend.repositories.country;

import com.hackathon.backend.entities.country.CountryDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CountryDetailsRepository extends JpaRepository<CountryDetailsEntity, Integer> {
    Optional<CountryDetailsEntity> findByCountryId(int countryId);

    void deleteByCountryId(int countryId);
}
