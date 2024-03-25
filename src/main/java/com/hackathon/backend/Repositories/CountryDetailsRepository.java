package com.hackathon.backend.Repositories;

import com.hackathon.backend.RelationShips.CountryDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CountryDetailsRepository extends JpaRepository<CountryDetailsEntity, Integer> {
}
