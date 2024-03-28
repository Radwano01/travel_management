package com.hackathon.backend.Repositories;

import com.hackathon.backend.Entities.UserTodosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserTodosRepository extends JpaRepository<UserTodosEntity,Integer> {
    Optional<List<UserTodosEntity>> findAllByUserId(int userId);
}
