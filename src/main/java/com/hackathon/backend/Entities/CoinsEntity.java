package com.hackathon.backend.Entities;


import jakarta.persistence.*;

@Entity
@Table(name = "coins")
public class CoinsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int coins;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private UserEntity userEntity;

    @Column(name = "userId")
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
