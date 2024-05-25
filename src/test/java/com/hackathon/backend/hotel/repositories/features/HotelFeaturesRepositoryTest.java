package com.hackathon.backend.hotel.repositories.features;

import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.repositories.hotel.hotelFeatures.HotelFeaturesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HotelFeaturesRepositoryTest {

    @Autowired
    HotelFeaturesRepository hotelFeaturesRepository;

    @Test
    void existsHotelFeatureByHotelFeatures() {
        //given
        HotelFeaturesEntity hotelFeatures = new HotelFeaturesEntity("testFeature");
        hotelFeatures.setId(1);
        hotelFeaturesRepository.save(hotelFeatures);

        //when
        boolean response = hotelFeaturesRepository.existsHotelFeatureByHotelFeatures("testFeature");

        //then
        assertTrue(response);

        hotelFeaturesRepository.deleteAll();
    }
}