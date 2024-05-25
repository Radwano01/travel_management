package com.hackathon.backend.hotel.services.features;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.services.hotel.hotelFeatures.RoomFeaturesRelationsService;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.features.RoomFeaturesUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RoomFeaturesRelationsServiceTest {

    @Mock
    HotelUtils hotelUtils;

    @Mock
    RoomFeaturesUtils roomFeaturesUtils;

    @InjectMocks
    RoomFeaturesRelationsService roomFeaturesRelationsService;

    @Test
    void addRoomFeatureToHotel() {
        //given
        long hotelId = 1;
        int featureId = 1;

        RoomDetailsEntity roomDetails = new RoomDetailsEntity();

        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        hotel.setRoomDetails(roomDetails);

        RoomFeaturesEntity roomFeature = new RoomFeaturesEntity();
        roomFeature.setId(featureId);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        when(roomFeaturesUtils.findById(featureId)).thenReturn(roomFeature);

        //when
        ResponseEntity<?> response = roomFeaturesRelationsService.addRoomFeatureToHotel(hotelId, featureId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(hotelUtils).findHotelById(hotelId);
        verify(roomFeaturesUtils).findById(featureId);
        verify(hotelUtils).save(hotel);
        verify(roomFeaturesUtils).save(roomFeature);
    }

    @Test
    void removeRoomFeatureFromHotel() {
        //given
        long hotelId = 1;
        int featureId = 1;

        RoomDetailsEntity roomDetails = new RoomDetailsEntity();

        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        hotel.setRoomDetails(roomDetails);

        RoomFeaturesEntity roomFeature = new RoomFeaturesEntity();
        roomFeature.setId(featureId);

        roomDetails.getRoomFeatures().add(roomFeature);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        when(roomFeaturesUtils.findById(featureId)).thenReturn(roomFeature);

        //when
        ResponseEntity<?> response = roomFeaturesRelationsService.removeRoomFeatureFromHotel(hotelId, featureId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(hotelUtils).findHotelById(hotelId);
        verify(roomFeaturesUtils).findById(featureId);
        verify(hotelUtils).save(hotel);
        verify(roomFeaturesUtils).save(roomFeature);
    }

}