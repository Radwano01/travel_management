package com.hackathon.backend.hotel.services;

import com.hackathon.backend.dto.hotelDto.HotelDto;
import com.hackathon.backend.dto.hotelDto.RoomDetailsDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.services.hotel.HotelService;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.hotel.HotelEvaluationUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.RoomUtils;
import com.hackathon.backend.utilities.hotel.features.HotelFeaturesUtils;
import com.hackathon.backend.utilities.hotel.features.RoomFeaturesUtils;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    CountryUtils countryUtils;

    @Mock
    HotelUtils hotelUtils;

    @Mock
    RoomDetailsUtils roomDetailsUtils;

    @Mock
    RoomUtils roomUtils;

    @Mock
    HotelEvaluationUtils hotelEvaluationUtils;

    @Mock
    HotelFeaturesUtils hotelFeaturesUtils;

    @Mock
    RoomFeaturesUtils roomFeaturesUtils;

    @InjectMocks
    HotelService hotelService;

    @Test
    void createHotel() {
        //given
        int countryId = 1;
        CountryEntity country = new CountryEntity();
        country.setId(countryId);

        HotelDto hotelDto = new HotelDto();
        hotelDto.setHotelName("testHotel");
        hotelDto.setMainImage("testMainImage");
        hotelDto.setDescription("testDesc");
        hotelDto.setHotelRoomsCount(10);
        hotelDto.setAddress("testAddress");

        RoomDetailsDto roomDetailsDto = new RoomDetailsDto();
        roomDetailsDto.setImageOne("testImageOne");
        roomDetailsDto.setImageTwo("testImageTwo");
        roomDetailsDto.setImageThree("testImageThree");
        roomDetailsDto.setImageFour("testImageFour");
        roomDetailsDto.setDescription("testDesc");
        roomDetailsDto.setPrice(100);

        hotelDto.setRoomDetails(roomDetailsDto);

        //behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);

        //when
        ResponseEntity<?> response = hotelService.createHotel(countryId, hotelDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getHotels() {
        //given
        int countryId = 1;
        CountryEntity country = new CountryEntity();
        country.setId(countryId);

        List<HotelDto> hotelDtos = new ArrayList<>();

        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(1);
        hotelDto.setHotelName("testHotel");
        hotelDto.setMainImage("testImage");
        hotelDto.setDescription("testDesc");
        hotelDto.setAddress("testAddress");
        hotelDto.setRate(2.50f);

        hotelDtos.add(hotelDto);

        //behavior
        when(hotelUtils.findByCountryId(countryId)).thenReturn(hotelDtos);

        //when
        ResponseEntity<?> response = hotelService.getHotels(countryId);

        List<HotelDto> hotelDtoList = (List<HotelDto>) response.getBody();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(hotelDto.getId(), hotelDtoList.get(0).getId());
        assertEquals(hotelDto.getHotelName(), hotelDtoList.get(0).getHotelName());
        assertEquals(hotelDto.getMainImage(), hotelDtoList.get(0).getMainImage());
        assertEquals(hotelDto.getDescription(), hotelDtoList.get(0).getDescription());
        assertEquals(hotelDto.getAddress(), hotelDtoList.get(0).getAddress());
        assertEquals(hotelDto.getRate(), hotelDtoList.get(0).getRate());
    }

    @Test
    void editHotel() {
        //given
        long hotelId = 1L;
        int countryId = 1;
        HotelDto hotelDto = new HotelDto();
        hotelDto.setHotelName("testHotel");
        hotelDto.setMainImage("testImage");
        hotelDto.setDescription("testDescription");
        hotelDto.setHotelRoomsCount(80);
        hotelDto.setAddress("testAddress");
        hotelDto.setRate(3.5f);

        HotelEntity hotel = new HotelEntity();
        hotel.setHotelName("testHotel");
        hotel.setMainImage("testImage");
        hotel.setDescription("testDesc");
        hotel.setHotelRoomsCount(80);
        hotel.setAddress("testAddress");
        hotel.setRate(3.5f);

        //behavior
        CountryEntity country = new CountryEntity();
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        when(countryUtils.findCountryById(countryId)).thenReturn(country);

        //when
        ResponseEntity<?> response = hotelService.editHotel(hotelId, countryId, hotelDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());


        assertEquals("testHotel", hotel.getHotelName());
        assertEquals("testImage", hotel.getMainImage());
        assertEquals("testDescription", hotel.getDescription());
        assertEquals(80, hotel.getHotelRoomsCount());
        assertEquals("testAddress", hotel.getAddress());
        assertEquals(3.5f, hotel.getRate());
    }

    @Test
    void deleteHotel() {
        //given
        long hotelId = 1L;
        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        CountryEntity country = new CountryEntity();
        hotel.setCountry(country);

        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        List<HotelFeaturesEntity> hotelFeatures = new ArrayList<>();
        List<RoomFeaturesEntity> roomFeatures = new ArrayList<>();
        roomDetails.setHotelFeatures(hotelFeatures);
        roomDetails.setRoomFeatures(roomFeatures);
        hotel.setRoomDetails(roomDetails);

        List<RoomEntity> rooms = new ArrayList<>();
        hotel.setRooms(rooms);
        List<HotelEvaluationEntity> evaluations = new ArrayList<>();
        hotel.setEvaluations(evaluations);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);

        //when
        ResponseEntity<?> response = hotelService.deleteHotel(hotelId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roomUtils, times(rooms.size())).delete(any(RoomEntity.class));
        verify(hotelEvaluationUtils, times(evaluations.size())).delete(any(HotelEvaluationEntity.class));
        verify(hotelFeaturesUtils, times(hotelFeatures.size())).save(any(HotelFeaturesEntity.class));
        verify(roomFeaturesUtils, times(roomFeatures.size())).save(any(RoomFeaturesEntity.class));
        verify(roomDetailsUtils).delete(roomDetails);
        verify(hotelUtils).delete(hotel);
        verify(countryUtils).save(country);
    }
}