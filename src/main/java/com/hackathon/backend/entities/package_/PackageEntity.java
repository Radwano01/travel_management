package com.hackathon.backend.entities.package_;

import com.hackathon.backend.entities.country.CountryEntity;
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
@Table(name = "package")
public class PackageEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String packageName;
    private int price;
    private String mainImage;
    private int rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @OneToOne(mappedBy = "packageOffer", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private PackageDetailsEntity packageDetails;

    @OneToMany(mappedBy = "packageEntity", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<PackageEvaluationEntity> packageEvaluations = new ArrayList<>();


    public PackageEntity(String packageName,
                         int price,
                         String mainImage,
                         int rate,
                         CountryEntity country){
        this.packageName = packageName;
        this.price = price;
        this.mainImage = mainImage;
        this.rate = rate;
        this.country = country;
    }

    @Override
    public String toString() {
        return "PackageEntity{" +
                "id=" + id +
                ", packageName='" + packageName + '\'' +
                ", price=" + price +
                ", mainImage='" + mainImage + '\'' +
                ", rate=" + rate +
                ", country=" + country +
                ", packageDetails=" + packageDetails +
                '}';
    }
}
