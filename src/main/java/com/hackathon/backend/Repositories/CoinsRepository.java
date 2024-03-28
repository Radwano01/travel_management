package com.hackathon.backend.Repositories;


import com.hackathon.backend.Entities.CoinsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoinsRepository extends JpaRepository<CoinsEntity, Integer> {
    CoinsEntity findByUserId(int userID);
}
