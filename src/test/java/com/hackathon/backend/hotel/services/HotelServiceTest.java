package com.hackathon.backend.hotel.services;

import com.hackathon.backend.dto.hotelDto.EditHotelDto;
import com.hackathon.backend.dto.hotelDto.PostHotelDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.services.hotel.HotelService;
import com.hackathon.backend.utilities.amazonServices.S3Service;
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
import org.springframework.mock.web.MockMultipartFile;

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

    @Mock
    S3Service s3Service;

    @InjectMocks
    HotelService hotelService;


    @Test
    void createHotel() {
        // given
        int countryId = 1;
        CountryEntity country = new CountryEntity();
        country.setId(countryId);

        PostHotelDto h = new PostHotelDto(
                "testHotel",
                new MockMultipartFile("mainImage", "mainImage.jpg", "image/jpeg", new byte[0]),
                "testDesc",
                10,
                "testAddress",
                0,
                new MockMultipartFile("imageOne", "imageOne.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageTwo", "imageTwo.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageThree", "imageThree.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageFour", "imageFour.jpg", "image/jpeg", new byte[0]),
                "testDesc",
                100
        );

        // Mock the behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);

        // when
        ResponseEntity<?> response = hotelService.createHotel(countryId, h);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void getHotels() {
        //given
        int countryId = 1;
        CountryEntity country = new CountryEntity();
        country.setId(countryId);

        List<GetHotelDto> getHotelDtos = new ArrayList<>();

        GetHotelDto getHotelDto = new GetHotelDto();
        getHotelDto.setId(1);
        getHotelDto.setHotelName("testHotel");
        getHotelDto.setMainImage("testImage");
        getHotelDto.setDescription("testDesc");
        getHotelDto.setAddress("testAddress");
        getHotelDto.setRate(2);

        getHotelDtos.add(getHotelDto);

        //behavior
        when(hotelUtils.findByCountryId(countryId)).thenReturn(getHotelDtos);

        //when
        ResponseEntity<?> response = hotelService.getHotels(countryId);

        List<GetHotelDto> getHotelDtoList = (List<GetHotelDto>) response.getBody();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getHotelDto.getId(), getHotelDtoList.get(0).getId());
        assertEquals(getHotelDto.getHotelName(), getHotelDtoList.get(0).getHotelName());
        assertEquals(getHotelDto.getMainImage(), getHotelDtoList.get(0).getMainImage());
        assertEquals(getHotelDto.getDescription(), getHotelDtoList.get(0).getDescription());
        assertEquals(getHotelDto.getAddress(), getHotelDtoList.get(0).getAddress());
        assertEquals(getHotelDto.getRate(), getHotelDtoList.get(0).getRate());
    }

    @Test
    void editHotel() {
        // given
        long hotelId = 1L;
        EditHotelDto editHotelDto = new EditHotelDto();
        editHotelDto.setHotelName("new");

        HotelEntity mockHotel = new HotelEntity();
        mockHotel.setHotelName("old");

        // behavior
        when(hotelUtils.checkHelper(editHotelDto)).thenReturn(true);
        when(hotelUtils.findHotelById(hotelId)).thenReturn(mockHotel);

        doAnswer(invocation -> {
            HotelEntity hotelEntity = invocation.getArgument(0);
            EditHotelDto dto = invocation.getArgument(1);

            hotelEntity.setHotelName(dto.getHotelName());

            return null;
        }).when(hotelUtils).editHelper(any(HotelEntity.class), any(EditHotelDto.class));

        // when
        ResponseEntity<?> response = hotelService.editHotel(hotelId, editHotelDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("new", mockHotel.getHotelName());
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