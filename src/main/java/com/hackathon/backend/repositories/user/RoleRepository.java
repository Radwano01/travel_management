package com.hackathon.backend.repositories.user;

import com.hackathon.backend.entities.user.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Integer> {
    Optional<RoleEntity> findByRole(String role);
}
