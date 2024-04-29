package com.hackathon.backend.repositories.user;

import com.hackathon.backend.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    boolean existsByEmail(String email);
    Optional<UserEntity> findUserByEmail(String email);
    Optional<UserEntity> findUserByUsername(String username);
    boolean existsUsernameByUsername(String username);
}