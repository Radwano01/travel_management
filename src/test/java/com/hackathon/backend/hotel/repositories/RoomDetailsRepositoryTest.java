package com.hackathon.backend.hotel.repositories;

import com.hackathon.backend.dto.hotelDto.features.GetHotelFeaturesDto;
import com.hackathon.backend.dto.hotelDto.features.GetRoomFeaturesDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
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
    RoomDetailsRepository roomDetailsRepository;

    @Autowired
    HotelFeaturesRepository hotelFeaturesRepository;

    @Autowired
    RoomFeaturesRepository roomFeaturesRepository;

    @Autowired
    HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        HotelFeaturesEntity hotelFeatures = new HotelFeaturesEntity();
        hotelFeatures.setHotelFeatures("test feature");
        hotelFeaturesRepository.save(hotelFeatures);

        RoomFeaturesEntity roomFeatures = new RoomFeaturesEntity();
        roomFeatures.setRoomFeatures("test feature");
        roomFeaturesRepository.save(roomFeatures);

        HotelEntity hotel = new HotelEntity();
        hotel.setHotelName("Test Hotel");
        hotelRepository.save(hotel);

        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        roomDetails.setHotel(hotel);
        roomDetails.getHotelFeatures().add(hotelFeatures);
        roomDetails.getRoomFeatures().add(roomFeatures);
        roomDetailsRepository.save(roomDetails);
    }

    @AfterEach
    void tearDown() {
        hotelFeaturesRepository.deleteAll();
        roomDetailsRepository.deleteAll();
        hotelFeaturesRepository.deleteAll();
    }

    @Test
    void findRoomDetailsHotelFeaturesByHotelId() {
        //given
        long hotelId = hotelRepository.findAll().get(0).getId();

        //when
        List<GetHotelFeaturesDto> response = roomDetailsRepository.findRoomDetailsHotelFeaturesByHotelId(hotelId);

        //then
        assertEquals(1, response.size());
        assertEquals("test feature", response.get(0).getHotelFeature());
    }

    @Test
    void findRoomDetailsRoomFeaturesByHotelId() {
        //given
        long hotelId = hotelRepository.findAll().get(0).getId();

        //when
        List<GetRoomFeaturesDto> response = roomDetailsRepository.findRoomDetailsRoomFeaturesByHotelId(hotelId);

        //then
        assertEquals(1, response.size());
        assertEquals("test feature", response.get(0).getRoomFeature());
    }
}