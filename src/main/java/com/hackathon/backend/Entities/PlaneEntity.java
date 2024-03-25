package com.hackathon.backend.Entities;

import com.hackathon.backend.RelationShips.VisaEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "plane")
public class PlaneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String planeName;
    private int sitsCount;

    private String airportLaunch;
    private String airportLand;
    private String timeLaunch;
    private String timeLand;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "plane_visa", joinColumns = @JoinColumn(name = "plane_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "visa_id", referencedColumnName = "id"))
    private List<VisaEntity> visas = new ArrayList<>();

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

    public int getSitsCount() {
        return sitsCount;
    }

    public void setSitsCount(int sitsCount) {
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

    public List<VisaEntity> getVisas() {
        return visas;
    }

    public void setVisas(List<VisaEntity> visas) {
        this.visas = visas;
    }
}
