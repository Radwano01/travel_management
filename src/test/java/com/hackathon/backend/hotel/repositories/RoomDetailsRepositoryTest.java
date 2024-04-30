package com.hackathon.backend.hotel.repositories;

import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
import com.hackathon.backend.repositories.hotel.hotelFeatures.HotelFeaturesRepository;
import com.hackathon.backend.repositories.hotel.hotelFeatures.RoomFeaturesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoomDetailsRepositoryTest {

    @Autowired
    private RoomDetailsRepository roomDetailsRepository;

    @Autowired
    private HotelFeaturesRepository hotelFeaturesRepository;

    @Autowired
    private RoomFeaturesRepository roomFeaturesRepository;

    @BeforeEach
    void setUp() {
        HotelFeaturesEntity hotelFeatures = new HotelFeaturesEntity();
        hotelFeatures.setHotelFeatures("test feature");
        hotelFeaturesRepository.save(hotelFeatures);

        RoomFeaturesEntity roomFeatures = new RoomFeaturesEntity();
        roomFeatures.setRoomFeatures("test feature");
        roomFeaturesRepository.save(roomFeatures);

        RoomDetailsEntity roomDetails1 = new RoomDetailsEntity();
        roomDetails1.getHotelFeatures().add(hotelFeatures);
        roomDetailsRepository.save(roomDetails1);

        RoomDetailsEntity roomDetails2 = new RoomDetailsEntity();
        roomDetails2.getRoomFeatures().add(roomFeatures);
        roomDetailsRepository.save(roomDetails2);
    }

    @AfterEach
    void tearDown() {
        roomDetailsRepository.deleteAll();
        hotelFeaturesRepository.deleteAll();
        roomFeaturesRepository.deleteAll();
    }

    @Test
    void findAllByHotelFeaturesId() {
        //given
        int hotelFeatureId = 1;
        //when
        List<RoomDetailsEntity> allHotelFeatures = roomDetailsRepository.findByHotelFeaturesId(hotelFeatureId);
        //then
        assertNotNull(allHotelFeatures);
    }

    @Test
    void findAllByRoomFeaturesId() {
        //given
        int roomFeatureId = 1;
        //when
        List<RoomDetailsEntity> allRoomFeatures = roomDetailsRepository.findByRoomFeaturesId(roomFeatureId);
        //then
        assertNotNull(allRoomFeatures);

    }
}