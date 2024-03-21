package com.hackathon.backend.Dto.PlaneDto;

public class VisaDto {

    private int id;
    private String airportLaunch;
    private String airportLand;
    private String timeLaunch;
    private String timeLand;
    private Integer placeNumber;
    private Integer price;
    private String planeName;
    private String Status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAirportLaunch() {
        return airportLaunch;
    }

    public void setAirportLaunch(String airportLaunch) {
        this.airportLaunch = airportLaunch;
    }

    public String getAirportLand() {
        return airportLand;
    }

    public void setAirportLand(String airportLand) {
        this.airportLand = airportLand;
    }

    public String getTimeLaunch() {
        return timeLaunch;
    }

    public void setTimeLaunch(String timeLaunch) {
        this.timeLaunch = timeLaunch;
    }

    public String getTimeLand() {
        return timeLand;
    }

    public void setTimeLand(String timeLand) {
        this.timeLand = timeLand;
    }

    public Integer getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(Integer placeNumber) {
        this.placeNumber = placeNumber;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPlaneName() {
        return planeName;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
