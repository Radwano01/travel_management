package com.hackathon.backend.repositories.hotel.hotelFeatures;


import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HotelFeaturesRepository extends JpaRepository<HotelFeaturesEntity, Integer> {
    boolean existsHotelFeatureByHotelFeatures(String hotelFeature);
}
