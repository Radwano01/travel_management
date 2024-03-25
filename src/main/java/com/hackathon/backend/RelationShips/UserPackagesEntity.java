package com.hackathon.backend.RelationShips;

import com.hackathon.backend.Entities.CountryEntity;
import com.hackathon.backend.Entities.PackageEntity;
import com.hackathon.backend.Entities.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "UserPayedPackages")
public class UserPackagesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity userEntity;

    @Column(name = "user_id")
    private int userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id", insertable = false, updatable = false)
    private PackageEntity packageEntity;

    @Column(name = "package_id")
    private int packageId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPackageID() {
        return packageId;
    }

    public void setPackageID(int packageID) {
        this.packageId = packageID;
    }
}
