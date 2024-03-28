package com.hackathon.backend.Entities;


import com.hackathon.backend.RelationShips.TodoListEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "userTodos")
public class UserTodosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", updatable = false, insertable = false)
    private UserEntity userEntity;

    @Column(name="userId")
    private int userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="todosId",updatable = false, insertable = false)
    private TodoListEntity todoListEntity;

    @Column(name="todosId")
    private int todosId;

    private boolean status = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTodosId() {
        return todosId;
    }

    public void setTodosId(int todosId) {
        this.todosId = todosId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
