package com.hackathon.backend.Entities;


import jakarta.persistence.*;

@Entity
@Table(name = "coins")
public class CoinsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int Coins;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username")
    private UserEntity username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoins() {
        return Coins;
    }

    public void setCoins(int coins) {
        Coins = coins;
    }

    public UserEntity getUsername() {
        return username;
    }

    public void setUsername(UserEntity username) {
        this.username = username;
    }
}
