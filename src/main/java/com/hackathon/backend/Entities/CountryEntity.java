package com.hackathon.backend.Entities;

import com.hackathon.backend.RelationShips.CountryDetailsEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "countries")
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "country")
    private String country;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "country_detail",
            joinColumns = @JoinColumn(name = "country_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "details_id", referencedColumnName = "id"))
    private List<CountryDetailsEntity> countryDetails = new ArrayList<>();

    public CountryEntity(){}

    public CountryEntity(String country){
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public List<CountryDetailsEntity> getCountryDetails() {
        return countryDetails;
    }

    public void setCountryDetails(List<CountryDetailsEntity> countryDetails) {
        this.countryDetails = countryDetails;
    }
}
