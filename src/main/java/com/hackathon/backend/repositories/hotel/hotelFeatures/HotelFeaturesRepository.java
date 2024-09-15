package com.hackathon.backend.repositories.hotel.hotelFeatures;


import com.hackathon.backend.dto.hotelDto.features.GetHotelFeaturesDto;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HotelFeaturesRepository extends JpaRepository<HotelFeaturesEntity, Integer> {

    boolean existsHotelFeatureByHotelFeatures(String hotelFeature);

    @Query("SELECT new com.hackathon.backend.dto.hotelDto.features.GetHotelFeaturesDto" +
            "(h.id, h.hotelFeatures)" +
            " FROM HotelFeaturesEntity h")
    List<GetHotelFeaturesDto> findAllHotelFeatures();
}
