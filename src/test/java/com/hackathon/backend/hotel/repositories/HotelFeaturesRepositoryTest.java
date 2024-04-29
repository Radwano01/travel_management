package com.hackathon.backend.hotel.repositories;

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
    private HotelFeaturesRepository hotelFeaturesRepository;

    @BeforeEach
    void setUp(){
        HotelFeaturesEntity hotelFeatures = new HotelFeaturesEntity();
        hotelFeatures.setHotelFeatures("test feature");
        hotelFeaturesRepository.save(hotelFeatures);
    }

    @AfterEach
    void tearDown(){
        hotelFeaturesRepository.deleteAll();
    }

    @Test
    void findByHotelFeatures() {
        // given
        String hotelFeature = "test feature";
        // when
        boolean existsHotelFeatures = hotelFeaturesRepository.existsHotelFeatureByHotelFeatures(hotelFeature);
        // then
        assertTrue(existsHotelFeatures);

    }
}