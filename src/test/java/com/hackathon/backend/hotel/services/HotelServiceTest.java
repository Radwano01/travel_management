package com.hackathon.backend.hotel.services;

import com.hackathon.backend.dto.hotelDto.HotelDto;
import com.hackathon.backend.dto.hotelDto.RoomDetailsDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.services.hotel.HotelService;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.RoomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private PlaceUtils placeUtils;

    @Mock
    private CountryUtils countryUtils;

    @Mock
    private RoomUtils roomUtils;

    @Mock
    private RoomDetailsUtils roomDetailsUtils;

    @Mock
    private HotelUtils hotelUtils;

    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        hotelService = new HotelService(placeUtils,
                countryUtils,
                roomUtils,
                hotelUtils,
                roomDetailsUtils);
    }

    @AfterEach
    void tearDown() {
        roomUtils.deleteAll();
        hotelUtils.deleteAll();
    }

    @Test
    void createHotel() {
        //given
        int countryId = 1;
        int placeId = 1;

        RoomDetailsDto roomDetailsDto = new RoomDetailsDto();
        roomDetailsDto.setImageOne("test");
        roomDetailsDto.setImageTwo("test");
        roomDetailsDto.setImageThree("test");
        roomDetailsDto.setImageFour("test");
        roomDetailsDto.setDescription("test");
        roomDetailsDto.setPrice(100);

        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(1);
        hotelDto.setHotelName("test");
        hotelDto.setMainImage("test");
        hotelDto.setTitle("test");
        hotelDto.setDescription("test");
        hotelDto.setHotelRoomsCount(100);
        hotelDto.setAddress("test");
        hotelDto.setRate(4.5f);
        hotelDto.setRoomDetails(roomDetailsDto);

        when(countryUtils.findCountryById(countryId)).thenReturn(new CountryEntity());
        when(placeUtils.findById(placeId)).thenReturn(new PlaceEntity());
        //when
        ResponseEntity<?> response = hotelService.createHotel(countryId, placeId, hotelDto);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getHotels() {
        //given
        int countryId = 1;
        HotelDto hotel = new HotelDto(
                1,
                "Grand Hotel",
                "grand_hotel.jpg",
                "Luxury at Its Best",
                "Experience unparalleled luxury and comfort at the Grand Hotel.",
                100,
                "123 Main Street, City",
                4.5f
        );

        when(hotelUtils.findByCountryId(countryId)).thenReturn((List.of(hotel)));
        //when
        ResponseEntity<?> response = hotelService.getHotels(countryId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        List<HotelDto> queryHotelDtos = (List<HotelDto>) response.getBody();
        //then
        HotelDto queryHotelDto = queryHotelDtos.get(0);
        assertEquals(hotel.getId(), queryHotelDto.getId());
        assertEquals(hotel.getHotelName(), queryHotelDto.getHotelName());
        assertEquals(hotel.getMainImage(), queryHotelDto.getMainImage());
        assertEquals(hotel.getTitle(), queryHotelDto.getTitle());
        assertEquals(hotel.getDescription(), queryHotelDto.getDescription());
        assertEquals(hotel.getAddress(), queryHotelDto.getAddress());
        assertEquals(hotel.getRate(), queryHotelDto.getRate());
    }


    @Test
    void editHotel() {
        //given
        long hotelId = 1;
        int countryId = 1;
        int placeId = 1;

        CountryEntity country = new CountryEntity();
        country.setId(countryId);
        PlaceEntity place = new PlaceEntity();
        place.setId(placeId);

        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(1);
        hotelDto.setHotelName("Grand Hotel");
        hotelDto.setMainImage("grand_hotel.jpg");
        hotelDto.setTitle("Luxury at Its Best");
        hotelDto.setDescription("Experience unparalleled luxury and comfort at the Grand Hotel.");
        hotelDto.setHotelRoomsCount(100);
        hotelDto.setAddress("123 Main Street, City");
        hotelDto.setRate(4.5f);
        hotelDto.setHotelRoomsCount(100);

        HotelEntity hotel = new HotelEntity();
        hotel.setCountry(country);
        hotel.setPlace(place);

        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        //when
        ResponseEntity<?> response = hotelService.editHotel(hotelId, countryId, placeId, hotelDto);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteHotel() {
        //given
        long hotelId = 1;
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setId(1);
        roomEntity.setStatus(false);

        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        roomDetails.setImageOne("test");
        roomDetails.setImageTwo("test");
        roomDetails.setImageThree("test");
        roomDetails.setImageFour("test");
        roomDetails.setDescription("test");
        roomDetails.setPrice(100);

        HotelEntity hotel = new HotelEntity();
        hotel.setId(1);
        hotel.setHotelName("test");
        hotel.setMainImage("test");
        hotel.setTitle("test");
        hotel.setDescription("test");
        hotel.setHotelRoomsCount(100);
        hotel.setAddress("test");
        hotel.setRate(4.5f);
        hotel.setRoomDetails(roomDetails);
        hotel.getRooms().add(roomEntity);

        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        when(roomDetailsUtils.findByHotelId(hotelId)).thenReturn(roomDetails);
        //when
        ResponseEntity<?> response = hotelService.deleteHotel(hotelId);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}