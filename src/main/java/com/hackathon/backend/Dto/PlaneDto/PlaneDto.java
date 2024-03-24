package com.hackathon.backend.Dto.PlaneDto;

public class PlaneDto {

    private int id;

    private String planeName;
    private Integer sitsCount;
    private String airportLaunch;
    private String airportLand;
    private String timeLaunch;
    private String timeLand;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaneName() {
        return planeName;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
    }

    public Integer getSitsCount() {
        return sitsCount;
    }

    public void setSitsCount(Integer sitsCount) {
        this.sitsCount = sitsCount;
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
}
