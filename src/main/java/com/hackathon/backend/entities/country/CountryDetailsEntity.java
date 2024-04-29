package com.hackathon.backend.entities.country;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "countryDetails")
@Getter
@Setter
@NoArgsConstructor
public class CountryDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imageOne;
    private String imageTwo;
    private String imageThree;

    @Column(length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
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
}
