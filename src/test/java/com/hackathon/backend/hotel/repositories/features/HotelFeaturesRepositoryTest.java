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
    private HotelFeaturesRepository hotelFeaturesRepository;

    @BeforeEach
    void setUp() {
        // Create and save a HotelFeaturesEntity instance
        HotelFeaturesEntity feature = new HotelFeaturesEntity();
        feature.setHotelFeatures("Free WiFi");
        hotelFeaturesRepository.save(feature);
    }

    @AfterEach
    void tearDown() {
        hotelFeaturesRepository.deleteAll();
    }

    @Test
    void itShouldReturnExistHotelFeatureByHotelFeature() {
        //given
        String featureToCheck = "Free WiFi";

        //when
        boolean response = hotelFeaturesRepository.existsHotelFeatureByHotelFeatures(featureToCheck);

        //then
        assertTrue(response);
    }

    @Test
    void itShouldReturnNotFoundHotelFeatureByHotelFeature() {
        //given
        String featureToCheck = "Non-existent Feature";

        //when
        boolean response = hotelFeaturesRepository.existsHotelFeatureByHotelFeatures(featureToCheck);

        //then
        assertFalse(response);
    }
}
