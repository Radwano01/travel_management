package com.hackathon.backend.entities.country;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "countryDetails")
public class CountryDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imageOne;
    private String imageTwo;
    private String imageThree;

    @Column(length = 2000)
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    public CountryDetailsEntity(String imageOne,
                                String imageTwo,
                                String imageThree,
                                String description,
                                CountryEntity country) {
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
        this.country = country;
    }

    @Override
    public String toString() {
        return "CountryDetailsEntity{" +
                "id=" + id +
                ", imageOne='" + imageOne + '\'' +
                ", imageTwo='" + imageTwo + '\'' +
                ", imageThree='" + imageThree + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
