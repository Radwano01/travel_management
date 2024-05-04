package com.hackathon.backend.hotel.services;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.services.hotel.RoomService;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private HotelUtils hotelUtils;

    @Mock
    private RoomUtils roomUtils;

    private RoomService roomService;

    @BeforeEach
    void setUp() {
        roomService = new RoomService(hotelUtils,
                roomUtils);
    }

    @AfterEach
    void tearDown() {
        hotelUtils.deleteAll();
        roomUtils.deleteAll();
    }

    @Test
    void addRoom() {
        //given
        long hotelId = 1L;

        when(hotelUtils.findHotelById(hotelId)).thenReturn(new HotelEntity());
        //when
        ResponseEntity<?> response = roomService.addRoom(hotelId);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editRoom() {
        //given
        long hotelId = 1L;
        long roomId = 1L;
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setId(roomId);
        HotelEntity hotel = new HotelEntity();
        hotel.getRooms().add(roomEntity);

        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        //when
        ResponseEntity<?> response = roomService.editRoom(hotelId,roomId);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteRooms() {
        //given
        long hotelId = 1L;
        long roomId = 1L;
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setId(roomId);
        HotelEntity hotel = new HotelEntity();
        hotel.getRooms().add(roomEntity);
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);

        //when
        ResponseEntity<?> response = roomService.deleteRooms(hotelId,roomId);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}