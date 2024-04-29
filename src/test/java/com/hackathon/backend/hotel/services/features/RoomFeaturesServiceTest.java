//package com.hackathon.backend.hotel.services.features;
//
//import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
//import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
//import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
//import com.hackathon.backend.repositories.hotel.hotelFeatures.RoomFeaturesRepository;
//import com.hackathon.backend.services.hotel.featuresSerivces.RoomFeaturesService;
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
//class RoomFeaturesServiceTest {
//
//    @Mock
//    private RoomDetailsRepository roomDetailsRepository;
//
//    @Mock
//    private RoomFeaturesRepository roomFeaturesRepository;
//
//    private RoomFeaturesService roomFeaturesService;
//
//    @BeforeEach
//    void setUp() {
//        roomFeaturesService = new RoomFeaturesService(roomDetailsRepository,
//                roomFeaturesRepository);
//    }
//
//    @AfterEach
//    void tearDown() {
//        roomDetailsRepository.deleteAll();
//        roomFeaturesRepository.deleteAll();
//    }
//
//    @Test
//    void createRoomFeature() {
//        //given
//        String feature = "test hotel feature";
//        //when
//        ResponseEntity<?> response = roomFeaturesService.createRoomFeature(feature);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void editRoomFeature() {
//        //given
//        int featureId = 1;
//        String feature = "edited test hotel feature";
//
//        when(roomFeaturesRepository.findById(featureId))
//                .thenReturn(Optional.of(new RoomFeaturesEntity()));
//        //when
//        ResponseEntity<?> response = roomFeaturesService.editRoomFeature(featureId,feature);
//
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void deleteRoomFeature() {
//        //given
//        int featureId = 1;
//        long roomDetailsId = 1L;
//
//        when(roomDetailsRepository.findById(roomDetailsId)).thenReturn(Optional.of(new RoomDetailsEntity()));
//        //when
//        ResponseEntity<?> response = roomFeaturesService.deleteRoomFeature(roomDetailsId,featureId);
//        //then
//        assertEquals(HttpStatus.OK ,response.getStatusCode());
//    }
//}