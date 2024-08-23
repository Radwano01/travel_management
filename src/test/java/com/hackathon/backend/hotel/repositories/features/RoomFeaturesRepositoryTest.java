package com.hackathon.backend.hotel.repositories.features;

import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.repositories.hotel.hotelFeatures.RoomFeaturesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoomFeaturesRepositoryTest {

    @Autowired
    RoomFeaturesRepository roomFeaturesRepository;

    @BeforeEach
    void setUp() {
        // Create and save a RoomFeaturesEntity instance
        RoomFeaturesEntity feature = new RoomFeaturesEntity();
        feature.setRoomFeatures("Free WiFi");
        roomFeaturesRepository.save(feature);
    }

    @AfterEach
    void tearDown() {
        roomFeaturesRepository.deleteAll();
    }

    @Test
    void itShouldReturnExistRoomFeatureByRoomFeature() {
        //given
        String featureToCheck = "Free WiFi";

        //when
        boolean response = roomFeaturesRepository.existsRoomFeatureByRoomFeatures(featureToCheck);

        //then
        assertTrue(response);
    }

    @Test
    void itShouldReturnNotFoundRoomFeatureByRoomFeature() {
        //given
        String featureToCheck = "Non-existent Feature";

        //when
        boolean response = roomFeaturesRepository.existsRoomFeatureByRoomFeatures(featureToCheck);

        //then
        assertFalse(response);
    }
}