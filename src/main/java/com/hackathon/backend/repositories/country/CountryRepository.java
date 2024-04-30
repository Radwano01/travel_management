package com.hackathon.backend.repositories.country;

import com.hackathon.backend.dto.countryDto.CountryDto;
import com.hackathon.backend.entities.country.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CountryRepository extends JpaRepository<CountryEntity,Integer> {
    boolean existsByCountry(String country);
    CountryEntity findByCountry(String newCountryDto);

    @Query("SELECT new com.hackathon.backend.dto.countryDto.CountryDto(c.id, c.country, c.mainImage) FROM CountryEntity c")
    List<CountryDto> findAllCountries();
}