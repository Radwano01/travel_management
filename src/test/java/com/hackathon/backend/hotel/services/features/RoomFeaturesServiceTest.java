package com.hackathon.backend.hotel.services.features;

import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.services.hotel.featuresSerivces.RoomFeaturesService;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.features.RoomFeaturesUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomFeaturesServiceTest {

    @Mock
    private RoomDetailsUtils roomDetailsUtils;

    @Mock
    private RoomFeaturesUtils roomFeaturesUtils;

    private RoomFeaturesService roomFeaturesService;

    @BeforeEach
    void setUp() {
        roomFeaturesService = new RoomFeaturesService(roomDetailsUtils,
                roomFeaturesUtils);
    }

    @AfterEach
    void tearDown() {
        roomDetailsUtils.deleteAll();
        roomDetailsUtils.deleteAll();
    }

    @Test
    void createRoomFeature() {
        //given
        String feature = "test hotel feature";
        //when
        ResponseEntity<?> response = roomFeaturesService.createRoomFeature(feature);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editRoomFeature() {
        //given
        int featureId = 1;
        String feature = "edited test hotel feature";

        when(roomFeaturesUtils.findById(featureId))
                .thenReturn(new RoomFeaturesEntity());
        //when
        ResponseEntity<?> response = roomFeaturesService.editRoomFeature(featureId,feature);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteRoomFeature() {
        //given
        int featureId = 1;

        when(roomFeaturesUtils.findById(featureId)).thenReturn(new RoomFeaturesEntity());
        //when
        ResponseEntity<?> response = roomFeaturesService.deleteRoomFeature(featureId);
        //then
        assertEquals(HttpStatus.OK ,response.getStatusCode());
    }
}