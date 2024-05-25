package com.hackathon.backend.hotel.services;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.services.hotel.RoomService;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    HotelUtils hotelUtils;

    @Mock
    RoomUtils roomUtils;

    @InjectMocks
    RoomService roomService;

    @Test
    void addRoom() {
        //given
        long hotelId = 1L;
        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        hotel.setHotelRoomsCount(5);
        List<RoomEntity> rooms = new ArrayList<>();
        hotel.setRooms(rooms);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);

        //when
        ResponseEntity<?> response = roomService.addRoom(hotelId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roomUtils).save(any(RoomEntity.class));
        verify(hotelUtils).save(hotel);
    }

    @Test
    void editRoom() {
        //given
        long hotelId = 1;
        long roomId = 1;
        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);

        RoomEntity room = new RoomEntity();
        room.setId(roomId);
        room.setStatus(false);
        hotel.getRooms().add(room);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);

        //when
        ResponseEntity<?> response = roomService.editRoom(hotelId, roomId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(room.isStatus());
    }

    @Test
    void deleteRooms() {
        //given
        long hotelId = 1L;
        long roomId = 1L;
        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);

        RoomEntity room = new RoomEntity();
        room.setId(roomId);
        hotel.getRooms().add(room);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);

        //when
        ResponseEntity<?> response = roomService.deleteRooms(hotelId, roomId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roomUtils).delete(room);
        verify(hotelUtils).save(hotel);
    }
}