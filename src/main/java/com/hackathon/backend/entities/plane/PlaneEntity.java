package com.hackathon.backend.entities.plane;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "planes")
public class PlaneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String planeCompanyName;
    private int numSeats;
    private int paidSeats = 0;
    private boolean status = true;

    @OneToOne(mappedBy = "plane", fetch = FetchType.LAZY)
    private PlaneFlightsEntity flight;

    public PlaneEntity(String planeCompanyName, int numSeats) {
        this.planeCompanyName = planeCompanyName;
        this.numSeats = numSeats;
    }

    @Override
    public String toString() {
        return "PlaneEntity{" +
                "id=" + id +
                ", planeCompanyName='" + planeCompanyName + '\'' +
                ", numSeats=" + numSeats +
                ", paidSeats=" + paidSeats +
                ", status=" + status +
                '}';
    }
}
