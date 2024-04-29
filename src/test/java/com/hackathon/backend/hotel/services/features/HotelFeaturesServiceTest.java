//package com.hackathon.backend.hotel.services.features;
//
//import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
//import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
//import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
//import com.hackathon.backend.repositories.hotel.hotelFeatures.HotelFeaturesRepository;
//import com.hackathon.backend.services.hotel.featuresSerivces.HotelFeaturesService;
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
//
//@ExtendWith(MockitoExtension.class)
//class HotelFeaturesServiceTest {
//
//    @Mock
//    private RoomDetailsRepository roomDetailsRepository;
//
//    @Mock
//    private HotelFeaturesRepository hotelFeaturesRepository;
//
//    private HotelFeaturesService hotelFeaturesService;
//
//    @BeforeEach
//    void setUp() {
//        hotelFeaturesService = new HotelFeaturesService(roomDetailsRepository,
//                hotelFeaturesRepository);
//    }
//
//    @AfterEach
//    void tearDown() {
//        roomDetailsRepository.deleteAll();
//        hotelFeaturesRepository.deleteAll();
//    }
//
//    @Test
//    void createHotelFeature() {
//        //given
//        String feature = "test hotel feature";
//        //when
//        ResponseEntity<?> response = hotelFeaturesService.createHotelFeature(feature);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void editHotelFeature() {
//        //given
//        int featureId = 1;
//        String feature = "edited test hotel feature";
//
//        when(hotelFeaturesRepository.findById(featureId))
//                .thenReturn(Optional.of(new HotelFeaturesEntity()));
//        //when
//        ResponseEntity<?> response = hotelFeaturesService.editHotelFeature(featureId,feature);
//
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//    }
//
//    @Test
//    void deleteHotelFeature() {
//        //given
//        int featureId = 1;
//        long roomDetailsId = 1L;
//
//        when(roomDetailsRepository.findById(roomDetailsId)).thenReturn(Optional.of(new RoomDetailsEntity()));
//        //when
//        ResponseEntity<?> response = hotelFeaturesService.deleteHotelFeature(roomDetailsId,featureId);
//        //then
//        assertEquals(HttpStatus.OK ,response.getStatusCode());
//    }
//}