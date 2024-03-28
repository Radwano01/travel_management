package com.hackathon.backend.Repositories;

import com.hackathon.backend.Entities.UserPackagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserPackagesRepository extends JpaRepository<UserPackagesEntity, Integer> {
    Optional<UserPackagesEntity> findByUserId(int userId);
}
