package com.hackathon.backend.hotel.services;

import com.hackathon.backend.dto.hotelDto.EditRoomDetailsDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.dto.hotelDto.GetRoomDetailsDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.services.hotel.RoomDetailsService;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomDetailsServiceTest {

    @Mock
    HotelUtils hotelUtils;

    @Mock
    RoomDetailsUtils roomDetailsUtils;

    @Mock
    S3Service s3Service;

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

        GetRoomDetailsDto getRoomDetailsDto = (GetRoomDetailsDto) response.getBody();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(roomDetails.getImageOne(), getRoomDetailsDto.getImageOne());
        assertEquals(roomDetails.getImageTwo(), getRoomDetailsDto.getImageTwo());
        assertEquals(roomDetails.getImageThree(), getRoomDetailsDto.getImageThree());
        assertEquals(roomDetails.getImageFour(), getRoomDetailsDto.getImageFour());
        assertEquals(roomDetails.getDescription(), getRoomDetailsDto.getDescription());
        assertEquals(roomDetails.getPrice(), getRoomDetailsDto.getPrice());
    }

    @Test
    void editRoomDetails() {
        //given
        long hotelId = 1L;
        EditRoomDetailsDto editRoomDetailsDto = new EditRoomDetailsDto(
                new MockMultipartFile("imageOne", "mainImage.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageTwo", "mainImage.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageThree", "mainImage.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageFour", "mainImage.jpg", "image/jpeg", new byte[0]),
                "testDesc",
                100
        );

        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        hotel.setRoomDetails(roomDetails);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        when(s3Service.uploadFile(editRoomDetailsDto.getImageOne())).thenReturn("imageOne");
        when(s3Service.uploadFile(editRoomDetailsDto.getImageTwo())).thenReturn("imageTwo");
        when(s3Service.uploadFile(editRoomDetailsDto.getImageThree())).thenReturn("imageThree");
        when(s3Service.uploadFile(editRoomDetailsDto.getImageFour())).thenReturn("imageFour");

        //when
        ResponseEntity<?> response = roomDetailsService.editRoomDetails(hotelId, editRoomDetailsDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("imageOne", roomDetails.getImageOne());
        assertEquals("imageTwo", roomDetails.getImageTwo());
        assertEquals("imageThree", roomDetails.getImageThree());
        assertEquals("imageFour", roomDetails.getImageFour());
        assertEquals("testDesc", roomDetails.getDescription());
        assertEquals(100, roomDetails.getPrice());
    }
}