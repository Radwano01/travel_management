package com.hackathon.backend.RelationShips;

import jakarta.persistence.*;

@Entity
@Table(name="roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
