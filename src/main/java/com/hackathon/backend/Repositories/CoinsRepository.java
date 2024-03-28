package com.hackathon.backend.Repositories;


import com.hackathon.backend.Entities.CoinsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinsRepository extends JpaRepository<CoinsEntity, Integer> {
}
