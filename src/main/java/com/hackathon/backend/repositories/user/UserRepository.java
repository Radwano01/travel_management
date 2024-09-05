package com.hackathon.backend.repositories.user;

import com.hackathon.backend.dto.userDto.UserDto;
import com.hackathon.backend.entities.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    boolean existsByEmail(String email);
    Optional<UserEntity> findUserByEmail(String email);
    Optional<UserEntity> findUserByUsername(String username);
    boolean existsUsernameByUsername(String username);

    @Query("SELECT new com.hackathon.backend.dto.userDto.UserDto" +
            "(u.id, u.username, u.email, u.image," +
            " u.verificationStatus, u.fullName, u.country," +
            " u.phoneNumber, u.address, u.dateOfBirth)" +
            " FROM UserEntity u WHERE u.id = :userId")
    UserDto findUserDetailsById(long userId);


    @Query("SELECT u.id FROM UserEntity u WHERE u.email = :email")
    Optional<UserEntity> findUserIdByEmail(String email);
}