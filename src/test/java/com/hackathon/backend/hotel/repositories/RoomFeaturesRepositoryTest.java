package com.hackathon.backend.hotel.repositories;

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
    private RoomFeaturesRepository roomFeaturesRepository;

    @BeforeEach
    void setUp() {
        RoomFeaturesEntity roomFeaturesEntity = new RoomFeaturesEntity();
        roomFeaturesEntity.setRoomFeatures("test feature");
        roomFeaturesRepository.save(roomFeaturesEntity);
    }

    @AfterEach
    void tearDown() {
        roomFeaturesRepository.deleteAll();
    }

    @Test
    void findByRoomFeatures() {
        //given
        String roomFeature = "test feature";
        //when
        boolean roomFeaturesOptional = roomFeaturesRepository.existsRoomFeatureByRoomFeatures(roomFeature);
        //then
        assertTrue(roomFeaturesOptional);

    }
}