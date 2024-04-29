//package com.hackathon.backend.hotel.services;
//
//import com.hackathon.backend.dto.hotelDto.HotelDto;
//import com.hackathon.backend.entities.country.CountryEntity;
//import com.hackathon.backend.entities.country.PlaceEntity;
//import com.hackathon.backend.entities.hotel.HotelEntity;
//import com.hackathon.backend.entities.hotel.RoomEntity;
//import com.hackathon.backend.repositories.country.PlaceRepository;
//import com.hackathon.backend.repositories.hotel.HotelRepository;
//import com.hackathon.backend.repositories.hotel.RoomRepository;
//import com.hackathon.backend.services.hotel.HotelService;
//import com.hackathon.backend.utilities.country.CountryUtils;
//import com.hackathon.backend.utilities.hotel.HotelUtils;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//
//@ExtendWith(MockitoExtension.class)
//class HotelServiceTest {
//
//    @Mock
//    private HotelRepository hotelRepository;
//
//    @Mock
//    private PlaceRepository placeRepository;
//
//    @Mock
//    private CountryUtils countryUtils;
//
//    @Mock
//    private RoomRepository roomRepository;
//
//    @Mock
//    private HotelUtils hotelUtils;
//
//    private HotelService hotelService;
//
//    @BeforeEach
//    void setUp() {
//        hotelService = new HotelService(hotelRepository,
//                placeRepository,
//                countryUtils,
//                roomRepository,
//                hotelUtils);
//    }
//
//    @AfterEach
//    void tearDown() {
//        roomRepository.deleteAll();
//        hotelRepository.deleteAll();
//    }
//
//    @Test
//    void createHotel() {
//        //given
//        int countryId = 1;
//        int placeId = 1;
//        HotelDto hotelDto = new HotelDto();
//        hotelDto.setId(1);
//        hotelDto.setHotelName("Grand Hotel");
//        hotelDto.setMainImage("grand_hotel.jpg");
//        hotelDto.setTitle("Luxury at Its Best");
//        hotelDto.setDescription("Experience unparalleled luxury and comfort at the Grand Hotel.");
//        hotelDto.setHotelRoomsCount(100);
//        hotelDto.setAddress("123 Main Street, City");
//        hotelDto.setRate(4.5f);
//
//        when(countryUtils.findCountryById(countryId)).thenReturn(new CountryEntity());
//        //when
//        ResponseEntity<?> response = hotelService.createHotel(countryId,placeId, hotelDto);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void getHotels() {
//        //given
//        int countryId = 1;
//        HotelDto hotel = new HotelDto(
//                1,
//                "Grand Hotel",
//                "grand_hotel.jpg",
//                "Luxury at Its Best",
//                "Experience unparalleled luxury and comfort at the Grand Hotel.",
//                100,
//                "123 Main Street, City",
//                4.5f
//        );
//
//        when(hotelRepository.findByCountryId(countryId)).thenReturn((List.of(hotel)));
//        //when
//        ResponseEntity<?> response = hotelService.getHotels(countryId);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        List<HotelDto> queryHotelDtos = (List<HotelDto>) response.getBody();
//        //then
//        HotelDto queryHotelDto = queryHotelDtos.get(0);
//        assertEquals(hotel.getId(), queryHotelDto.getId());
//        assertEquals(hotel.getHotelName(), queryHotelDto.getHotelName());
//        assertEquals(hotel.getMainImage(), queryHotelDto.getMainImage());
//        assertEquals(hotel.getTitle(), queryHotelDto.getTitle());
//        assertEquals(hotel.getDescription(), queryHotelDto.getDescription());
//        assertEquals(hotel.getAddress(), queryHotelDto.getAddress());
//        assertEquals(hotel.getRate(), queryHotelDto.getRate());
//    }
//
//
//    @Test
//    void editHotel() {
//        //given
//        long hotelId = 1;
//        int countryId = 1;
//        int placeId = 1;
//
//        CountryEntity country = new CountryEntity();
//        country.setId(countryId);
//        PlaceEntity place = new PlaceEntity();
//        place.setId(placeId);
//
//        HotelDto hotelDto = new HotelDto();
//        hotelDto.setId(1);
//        hotelDto.setHotelName("Grand Hotel");
//        hotelDto.setMainImage("grand_hotel.jpg");
//        hotelDto.setTitle("Luxury at Its Best");
//        hotelDto.setDescription("Experience unparalleled luxury and comfort at the Grand Hotel.");
//        hotelDto.setHotelRoomsCount(100);
//        hotelDto.setAddress("123 Main Street, City");
//        hotelDto.setRate(4.5f);
//        hotelDto.setHotelRoomsCount(100);
//
//        HotelEntity hotel = new HotelEntity();
//        hotel.setCountry(country);
//        hotel.setPlace(place);
//
//        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
//        //when
//        ResponseEntity<?> response = hotelService.editHotel(hotelId, countryId, placeId, hotelDto);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void deleteHotel() {
//        //given
//        long hotelId = 1;
//        RoomEntity roomEntity = new RoomEntity();
//        roomEntity.setId(1);
//        roomEntity.setStatus(false);
//        HotelEntity hotel = new HotelEntity();
//        hotel.setId(1);
//        hotel.setHotelName("Grand Hotel");
//        hotel.setMainImage("grand_hotel.jpg");
//        hotel.setTitle("Luxury at Its Best");
//        hotel.setDescription("Experience unparalleled luxury and comfort at the Grand Hotel.");
//        hotel.setHotelRoomsCount(100);
//        hotel.setAddress("123 Main Street, City");
//        hotel.setRate(4.5f);
//        hotel.getRooms().add(roomEntity);
//
//        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
//        //when
//        ResponseEntity<?> response = hotelService.deleteHotel(hotelId);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//}