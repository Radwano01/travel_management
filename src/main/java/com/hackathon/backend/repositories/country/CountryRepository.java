package com.hackathon.backend.repositories.country;

import com.hackathon.backend.dto.countryDto.GetCountryDto;
import com.hackathon.backend.entities.country.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CountryRepository extends JpaRepository<CountryEntity,Integer> {
    boolean existsByCountry(String country);
    @Query("SELECT new com.hackathon.backend.dto.countryDto.GetCountryDto(c.id, c.country, c.mainImage) FROM CountryEntity c")
    List<GetCountryDto> findAllCountries();
}
