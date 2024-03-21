package com.hackathon.backend.Entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "visa")
public class VisaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String airportLaunch;
    private String airportLand;
    private String timeLaunch;
    private String timeLand;
    private Integer placeNumber;
    private Integer price;
    private String status;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "visa_plane", joinColumns = @JoinColumn(name = "visa_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "plane_id", referencedColumnName = "id"))
    private List<PlaneEntity> planeEntityList = new ArrayList<>();


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PlaneEntity> getPlaneEntityList() {
        return planeEntityList;
    }

    public void setPlaneEntityList(List<PlaneEntity> planeEntityList) {
        this.planeEntityList = planeEntityList;
    }



}
