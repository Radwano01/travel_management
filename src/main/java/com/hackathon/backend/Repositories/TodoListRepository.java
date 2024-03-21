package com.hackathon.backend.Repositories;

import com.hackathon.backend.Entities.CountryEntity;
import com.hackathon.backend.Entities.TodoListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoListRepository extends JpaRepository<TodoListEntity,Integer> {
    boolean existsByTodos(String todos);
}