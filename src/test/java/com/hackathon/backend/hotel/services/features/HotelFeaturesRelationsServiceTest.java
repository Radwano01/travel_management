package com.hackathon.backend.hotel.services.features;

import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.repositories.hotel.hotelFeatures.HotelFeaturesRepository;
import com.hackathon.backend.services.hotel.featuresSerivces.HotelFeaturesRelationsService;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.features.HotelFeaturesUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HotelFeaturesRelationsServiceTest {

    @Mock
    private HotelFeaturesUtils hotelFeaturesUtils;

    @Mock
    private HotelUtils hotelUtils;

    private HotelFeaturesRelationsService hotelFeaturesRelationsService;

    @BeforeEach
    void setUp() {
        hotelFeaturesRelationsService = new HotelFeaturesRelationsService(
                hotelFeaturesUtils,
                hotelUtils);
    }

    @AfterEach
    void tearDown() {
        hotelFeaturesUtils.deleteAll();
    }

    @Test
    void addHotelFeatureToHotel() {
        //given
        long hotelId = 1L;
        int featureId = 1;
        HotelEntity hotel = new HotelEntity();
        hotel.setRoomDetails(new RoomDetailsEntity());
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        when(hotelFeaturesUtils.findById(featureId)).thenReturn(new HotelFeaturesEntity());
        //when
        ResponseEntity<?> response = hotelFeaturesRelationsService.addHotelFeatureToHotel(hotelId,featureId);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void removeHotelFeatureToHotel() {
        //given
        long hotelId = 1L;
        int featureId = 1;
        HotelFeaturesEntity hotelFeatures = new HotelFeaturesEntity();
        hotelFeatures.setId(featureId);

        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        roomDetails.getHotelFeatures().add(hotelFeatures);

        HotelEntity hotel = new HotelEntity();
        hotel.setRoomDetails(roomDetails);

        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        when(hotelFeaturesUtils.findById(featureId)).thenReturn(hotelFeatures);
        //when
        ResponseEntity<?> response = hotelFeaturesRelationsService.removeHotelFeatureFromHotel(hotelId,featureId);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}