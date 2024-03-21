package com.hackathon.backend.Repositories;

import com.hackathon.backend.Entities.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CountryRepository extends JpaRepository<CountryEntity,Integer> {
    Optional<CountryEntity> findByCountry(String country);

    boolean existsByCountry(String country);
}
