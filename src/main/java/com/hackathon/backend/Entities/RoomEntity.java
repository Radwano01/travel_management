package com.hackathon.backend.Entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int floor;
    private int doorNumber;
    private int roomsNumber;
    private int bathroomsNumber;
    private int bedsNumber;
    private int price;
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity userEntity;

    @Column(name = "user_id")
    private int userId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "hotel_room", joinColumns = @JoinColumn(name = "room_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "hotel_id", referencedColumnName = "id"))
    private List<HotelEntity> hotelEntityList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(int doorNumber) {
        this.doorNumber = doorNumber;
    }

    public int getRoomsNumber() {
        return roomsNumber;
    }

    public void setRoomsNumber(int roomsNumber) {
        this.roomsNumber = roomsNumber;
    }

    public int getBathroomsNumber() {
        return bathroomsNumber;
    }

    public void setBathroomsNumber(int bathroomsNumber) {
        this.bathroomsNumber = bathroomsNumber;
    }

    public int getBedsNumber() {
        return bedsNumber;
    }

    public void setBedsNumber(int bedsNumber) {
        this.bedsNumber = bedsNumber;
    }

    public List<HotelEntity> getHotelEntityList() {
        return hotelEntityList;
    }

    public void setHotelEntityList(List<HotelEntity> hotelEntityList) {
        this.hotelEntityList = hotelEntityList;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
