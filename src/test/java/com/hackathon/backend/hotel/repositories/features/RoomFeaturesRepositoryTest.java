package com.hackathon.backend.hotel.repositories.features;

import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.repositories.hotel.hotelFeatures.RoomFeaturesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoomFeaturesRepositoryTest {

    @Autowired
    RoomFeaturesRepository roomFeaturesRepository;

    @Test
    void existsHotelFeatureByHotelFeatures() {
        //given
        RoomFeaturesEntity roomFeaturesEntity = new RoomFeaturesEntity("testFeature");
        roomFeaturesEntity.setId(1);
        roomFeaturesRepository.save(roomFeaturesEntity);

        //when
        boolean response = roomFeaturesRepository.existsRoomFeatureByRoomFeatures("testFeature");

        //then
        assertTrue(response);

        roomFeaturesRepository.deleteAll();
    }
}