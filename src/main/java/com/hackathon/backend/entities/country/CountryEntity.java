package com.hackathon.backend.entities.country;

import com.hackathon.backend.entities.package_.PackageEntity;
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
@Table(name = "countries")
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String country;

    private String mainImage;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PackageEntity> packages = new ArrayList<>();

    @OneToOne(mappedBy ="country", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private CountryDetailsEntity countryDetails;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PlaceEntity> places = new ArrayList<>();

    public CountryEntity(String country,
                         String mainImage){
        this.country = country;
        this.mainImage = mainImage;
    }

    public CountryEntity(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "CountryEntity{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", countryDetails=" + countryDetails +
                '}';
    }
}