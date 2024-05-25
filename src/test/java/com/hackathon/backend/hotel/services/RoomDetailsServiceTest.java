package com.hackathon.backend.hotel.services;

import com.hackathon.backend.dto.hotelDto.HotelDto;
import com.hackathon.backend.dto.hotelDto.RoomDetailsDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.services.hotel.RoomDetailsService;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomDetailsServiceTest {

    @Mock
    HotelUtils hotelUtils;

    @Mock
    RoomDetailsUtils roomDetailsUtils;

    @InjectMocks
    RoomDetailsService roomDetailsService;

    @Test
    void getRoomAllDetails() {
        //given
        long hotelId = 1L;
        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        roomDetails.setImageOne("testImageOne");
        roomDetails.setImageTwo("testImageTwo");
        roomDetails.setImageThree("testImageThree");
        roomDetails.setImageFour("testImageFour");
        roomDetails.setDescription("testDesc");
        roomDetails.setPrice(100);
        hotel.setRoomDetails(roomDetails);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);

        //when
        ResponseEntity<?> response = roomDetailsService.getRoomAllDetails(hotelId);

        HotelDto hotelDto = (HotelDto) response.getBody();
        RoomDetailsDto roomDetailsDto = hotelDto.getRoomDetails();
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(hotel.getId(), hotelDto.getId());
        assertEquals(hotel.getHotelName(), hotelDto.getHotelName());
        assertEquals(hotel.getAddress(), hotelDto.getAddress());
        assertEquals(hotel.getRate(), hotelDto.getRate());

        assertEquals(roomDetails.getImageOne(), roomDetailsDto.getImageOne());
        assertEquals(roomDetails.getImageTwo(), roomDetailsDto.getImageTwo());
        assertEquals(roomDetails.getImageThree(), roomDetailsDto.getImageThree());
        assertEquals(roomDetails.getImageFour(), roomDetailsDto.getImageFour());
        assertEquals(roomDetails.getDescription(), roomDetailsDto.getDescription());
        assertEquals(roomDetails.getPrice(), roomDetailsDto.getPrice());
    }

    @Test
    void editRoomDetails() {
        //given
        long hotelId = 1L;
        RoomDetailsDto roomDetailsDto = new RoomDetailsDto();
        roomDetailsDto.setImageOne("testImageOne");
        roomDetailsDto.setImageTwo("testImageTwo");
        roomDetailsDto.setDescription("testDesc");
        roomDetailsDto.setPrice(150);

        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        hotel.setRoomDetails(roomDetails);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);

        //when
        ResponseEntity<?> response = roomDetailsService.editRoomDetails(hotelId, roomDetailsDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roomDetailsDto.getImageOne(), roomDetails.getImageOne());
        assertEquals(roomDetailsDto.getImageTwo(), roomDetails.getImageTwo());
        assertEquals(roomDetailsDto.getDescription(), roomDetails.getDescription());
        assertEquals(roomDetailsDto.getPrice(), roomDetails.getPrice());
    }
}