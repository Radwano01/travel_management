package com.hackathon.backend.hotel.services.features;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
import com.hackathon.backend.repositories.hotel.hotelFeatures.RoomFeaturesRepository;
import com.hackathon.backend.services.hotel.hotelFeatures.RoomFeaturesRelationsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomFeaturesRelationsServiceTest {

    @Mock
    private RoomFeaturesRepository roomFeaturesRepository;

    @Mock
    private RoomDetailsRepository roomDetailsRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private RoomFeaturesRelationsService roomFeaturesRelationsService;

    @Test
    void addRoomFeatureToHotel_Success() {
        // given
        long hotelId = 1L;
        int featureId = 1;
        HotelEntity hotel = new HotelEntity();
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        RoomFeaturesEntity roomFeatures = new RoomFeaturesEntity();

        hotel.setRoomDetails(roomDetails);
        roomDetails.getRoomFeatures().add(roomFeatures);

        //behavior
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(roomFeaturesRepository.findById(featureId)).thenReturn(Optional.of(roomFeatures));

        // when
        ResponseEntity<String> response = roomFeaturesRelationsService.addRoomFeatureToHotel(hotelId, featureId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Room feature added successfully " + roomFeatures.getRoomFeatures(), response.getBody());
        assertTrue(roomDetails.getRoomFeatures().contains(roomFeatures));

        verify(roomDetailsRepository).save(roomDetails);
    }

    @Test
    void removeRoomFeatureFromHotel_Success() {
        // given
        long hotelId = 1L;
        int featureId = 1;
        HotelEntity hotelEntity = new HotelEntity();
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        RoomFeaturesEntity roomFeatures = new RoomFeaturesEntity();
        roomFeatures.setId(featureId);

        roomDetails.getRoomFeatures().add(roomFeatures);
        hotelEntity.setRoomDetails(roomDetails);

        //behavior
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotelEntity));

        // when
        ResponseEntity<String> response = roomFeaturesRelationsService.removeRoomFeatureFromHotel(hotelId, featureId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Room feature removed from this hotel", response.getBody());
        assertTrue(roomDetails.getRoomFeatures().isEmpty());

        verify(roomDetailsRepository).save(roomDetails);
    }

    @Test
    void addRoomFeatureToHotel_FeatureAlreadyExists() {
        // given
        long hotelId = 1L;
        int featureId = 1;
        HotelEntity hotel = new HotelEntity();
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        RoomFeaturesEntity roomFeatures = new RoomFeaturesEntity();
        roomFeatures.setId(featureId);

        hotel.setRoomDetails(roomDetails);
        roomDetails.getRoomFeatures().add(roomFeatures);

        //behavior
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        // when
        ResponseEntity<String> response = roomFeaturesRelationsService.addRoomFeatureToHotel(hotelId, featureId);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("This Room feature is already valid for this hotel", response.getBody());

        verify(roomDetailsRepository, never()).save(roomDetails);
    }

    @Test
    void removeRoomFeatureFromHotel_FeatureNotFound() {
        // given
        long hotelId = 1L;
        int featureId = 1;
        HotelEntity hotelEntity = new HotelEntity();
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        hotelEntity.setRoomDetails(roomDetails);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotelEntity));

        // when
        ResponseEntity<String> response = roomFeaturesRelationsService.removeRoomFeatureFromHotel(hotelId, featureId);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("This feature is not found", response.getBody());

        verify(roomDetailsRepository, never()).save(roomDetails);
    }
}
