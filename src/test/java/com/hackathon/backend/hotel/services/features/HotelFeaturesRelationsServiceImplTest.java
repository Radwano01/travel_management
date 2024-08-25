package com.hackathon.backend.hotel.services.features;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
import com.hackathon.backend.repositories.hotel.hotelFeatures.HotelFeaturesRepository;
import com.hackathon.backend.services.hotel.hotelFeatures.impl.HotelFeaturesRelationsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelFeaturesRelationsServiceImplTest {

    @Mock
    private HotelFeaturesRepository hotelFeaturesRepository;

    @Mock
    private RoomDetailsRepository roomDetailsRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelFeaturesRelationsServiceImpl hotelFeaturesRelationsServiceImpl;

    @Test
    void addHotelFeatureToHotel_Success() {
        // given
        long hotelId = 1L;
        int featureId = 1;
        HotelEntity hotel = new HotelEntity();
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        HotelFeaturesEntity hotelFeatures = new HotelFeaturesEntity();

        hotel.setRoomDetails(roomDetails);
        roomDetails.getHotelFeatures().add(hotelFeatures);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(hotelFeaturesRepository.findById(featureId)).thenReturn(Optional.of(hotelFeatures));

        // when
        ResponseEntity<String> response = hotelFeaturesRelationsServiceImpl.addHotelFeatureToHotel(hotelId, featureId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hotel feature added successfully " + hotelFeatures.getHotelFeatures(), response.getBody());
        assertTrue(roomDetails.getHotelFeatures().contains(hotelFeatures));

        verify(roomDetailsRepository).save(roomDetails);
    }
    @Test
    void addHotelFeatureToHotel_FeatureAlreadyExists() {
        // given
        long hotelId = 1L;
        int featureId = 1;
        HotelEntity hotel = new HotelEntity();
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        HotelFeaturesEntity hotelFeatures = new HotelFeaturesEntity();
        hotelFeatures.setId(featureId);

        roomDetails.getHotelFeatures().add(hotelFeatures);

        hotel.setRoomDetails(roomDetails);


        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        // when
        ResponseEntity<String> response = hotelFeaturesRelationsServiceImpl.addHotelFeatureToHotel(hotelId, featureId);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("This Feature is already existed", response.getBody());

        verify(roomDetailsRepository, never()).save(any(RoomDetailsEntity.class));
    }


    @Test
    void removeHotelFeatureFromHotel_Success() {
        // given
        long hotelId = 1L;
        int featureId = 1;
        HotelEntity hotelEntity = new HotelEntity();
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        HotelFeaturesEntity hotelFeatures = new HotelFeaturesEntity();
        hotelFeatures.setId(featureId);

        roomDetails.getHotelFeatures().add(hotelFeatures);

        hotelEntity.setRoomDetails(roomDetails);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotelEntity));

        // when
        ResponseEntity<String> response = hotelFeaturesRelationsServiceImpl.removeHotelFeatureFromHotel(hotelId, featureId);

        // then
        assertEquals("Hotel feature removed successfully", response.getBody());
        assertTrue(roomDetails.getHotelFeatures().isEmpty());

        verify(roomDetailsRepository).save(roomDetails);
    }

    @Test
    void removeHotelFeatureFromHotel_FeatureNotFound() {
        // given
        long hotelId = 1L;
        int featureId = 1;
        HotelEntity hotelEntity = new HotelEntity();
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();

        hotelEntity.setRoomDetails(roomDetails);
        roomDetails.setHotelFeatures(new ArrayList<>());

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotelEntity));

        // when
        ResponseEntity<String> response = hotelFeaturesRelationsServiceImpl.removeHotelFeatureFromHotel(hotelId, featureId);

        // then
        assertEquals("This feature is not found", response.getBody());

        verify(roomDetailsRepository, never()).save(any(RoomDetailsEntity.class));
    }
}