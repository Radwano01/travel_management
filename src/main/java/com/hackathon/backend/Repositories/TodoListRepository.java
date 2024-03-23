package com.hackathon.backend.Repositories;

import com.hackathon.backend.RelationShips.TodoListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListRepository extends JpaRepository<TodoListEntity,Integer> {
    boolean existsByTodos(String todos);
}