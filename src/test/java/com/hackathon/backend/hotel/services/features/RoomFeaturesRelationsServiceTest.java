//package com.hackathon.backend.hotel.services.features;
//
//import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
//import com.hackathon.backend.entities.hotel.HotelEntity;
//import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
//import com.hackathon.backend.repositories.hotel.hotelFeatures.RoomFeaturesRepository;
//import com.hackathon.backend.services.hotel.featuresSerivces.RoomFeaturesRelationsService;
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
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class RoomFeaturesRelationsServiceTest {
//
//    @Mock
//    private RoomFeaturesRepository roomFeaturesRepository;
//
//    @Mock
//    private HotelUtils hotelUtils;
//
//    private RoomFeaturesRelationsService roomFeaturesRelationsService;
//
//    @BeforeEach
//    void setUp() {
//        roomFeaturesRelationsService = new RoomFeaturesRelationsService(hotelUtils,
//                roomFeaturesRepository);
//    }
//
//    @AfterEach
//    void tearDown() {
//        roomFeaturesRepository.deleteAll();
//    }
//
//    @Test
//    void addRoomFeatureToHotel() {
//        //given
//        long hotelId = 1L;
//        int featureId = 1;
//        HotelEntity hotel = new HotelEntity();
//        hotel.setRoomDetails(new RoomDetailsEntity());
//        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
//        when(roomFeaturesRepository.findById(featureId)).thenReturn(Optional.of(new RoomFeaturesEntity()));
//        //when
//        ResponseEntity<?> response = roomFeaturesRelationsService.addRoomFeatureToHotel(hotelId,featureId);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void deleteRoomFeatureToHotel() {
//        //given
//        long hotelId = 1L;
//        int featureId = 1;
//        HotelEntity hotel = new HotelEntity();
//        hotel.setRoomDetails(new RoomDetailsEntity());
//        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
//        when(roomFeaturesRepository.findById(featureId)).thenReturn(Optional.of(new RoomFeaturesEntity()));
//        //when
//        ResponseEntity<?> response = roomFeaturesRelationsService.removeRoomFeatureToHotel(hotelId,featureId);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//}