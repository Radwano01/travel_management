package com.hackathon.backend.hotel.services;

import com.hackathon.backend.dto.hotelDto.RoomDetailsDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
import com.hackathon.backend.services.hotel.RoomDetailsService;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
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
class RoomDetailsServiceTest {

    @Mock
    private HotelUtils hotelUtils;

    @Mock
    private RoomDetailsUtils roomDetailsUtils;

    private RoomDetailsService roomDetailsService;


    @BeforeEach
    void setUp() {
        roomDetailsService = new RoomDetailsService(hotelUtils,
                roomDetailsUtils);

    }

    @AfterEach
    void tearDown() {
        hotelUtils.deleteAll();
        roomDetailsUtils.deleteAll();
    }

    @Test
    void testGetAllHotelDetails_Success() {
        long hotelId = 1L;
        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        hotel.setHotelName("Test Hotel");

        RoomDetailsEntity roomDetails = new RoomDetailsEntity();

        hotel.setRoomDetails(roomDetails);

        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);

        ResponseEntity<?> response = roomDetailsService.getRoomAllDetails(hotelId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editRoomDetails() {
        //given
        long roomDetailsId = 1L;
        RoomDetailsDto roomDetailsDto = new RoomDetailsDto();
        roomDetailsDto.setImageOne("image_one");
        roomDetailsDto.setImageTwo("image_two");
        roomDetailsDto.setImageThree("image_three");
        roomDetailsDto.setImageFour("image_four");
        roomDetailsDto.setDescription("description");
        roomDetailsDto.setPrice(100);

        when(roomDetailsUtils.findById(roomDetailsId)).thenReturn(new RoomDetailsEntity());
        //when
        ResponseEntity<?> response = roomDetailsService.editRoomDetails(roomDetailsId,roomDetailsDto);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}