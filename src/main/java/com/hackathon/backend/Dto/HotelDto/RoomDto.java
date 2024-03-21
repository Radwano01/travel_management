package com.hackathon.backend.Dto.HotelDto;

public class RoomDto {

    private Integer id;
    private Integer floor;
    private Integer doorNumber;
    private Integer roomsNumber;
    private Integer bathroomsNumber;
    private Integer bedsNumber;
    private String hotelName;
    private Integer price;
    private String status;

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(Integer doorNumber) {
        this.doorNumber = doorNumber;
    }

    public Integer getRoomsNumber() {
        return roomsNumber;
    }

    public void setRoomsNumber(Integer roomsNumber) {
        this.roomsNumber = roomsNumber;
    }

    public Integer getBathroomsNumber() {
        return bathroomsNumber;
    }

    public void setBathroomsNumber(Integer bathroomsNumber) {
        this.bathroomsNumber = bathroomsNumber;
    }

    public Integer getBedsNumber() {
        return bedsNumber;
    }

    public void setBedsNumber(Integer bedsNumber) {
        this.bedsNumber = bedsNumber;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
