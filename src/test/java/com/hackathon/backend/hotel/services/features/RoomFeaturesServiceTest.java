package com.hackathon.backend.hotel.services.features;

import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.services.hotel.hotelFeatures.RoomFeaturesService;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomFeaturesServiceTest {

    @Mock
    RoomFeaturesUtils roomFeaturesUtils;

    @Mock
    RoomDetailsUtils roomDetailsUtils;

    @InjectMocks
    RoomFeaturesService roomFeaturesService;

    @Test
    void createRoomFeature() {
        //given
        String feature = "testFeature";

        //behavior
        when(roomFeaturesUtils.existsRoomFeatureByRoomFeatures(feature)).thenReturn(false);

        //when
        ResponseEntity<?> response = roomFeaturesService.createRoomFeature(feature);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getRoomFeatures() {
        //given
        RoomFeaturesEntity roomFeature = new RoomFeaturesEntity();

        List<RoomFeaturesEntity> features = new ArrayList<>();
        features.add(roomFeature);

        //behavior
        when(roomFeaturesUtils.findAll()).thenReturn(features);

        //when
        ResponseEntity<?> response = roomFeaturesService.getRoomFeatures();

        //then
        assertEquals(ResponseEntity.ok(features), response);
        verify(roomFeaturesUtils).findAll();
    }

    @Test
    void editRoomFeature() {
        //given
        int featureId = 1;
        String featureName = "testFeature";

        RoomFeaturesEntity roomFeatures = new RoomFeaturesEntity();
        roomFeatures.setId(featureId);

        //behavior
        when(roomFeaturesUtils.findById(featureId)).thenReturn(roomFeatures);

        //when
        ResponseEntity<?> response = roomFeaturesService.editRoomFeature(featureId, featureName);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roomFeaturesUtils).findById(featureId);
        verify(roomFeaturesUtils).save(roomFeatures);
    }

    @Test
    void deleteRoomFeature() {
        int featureId = 1;

        RoomFeaturesEntity roomFeature = new RoomFeaturesEntity();
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();

        roomFeature.getRoomDetails().add(roomDetails);
        roomDetails.getRoomFeatures().add(roomFeature);

        when(roomFeaturesUtils.findById(featureId)).thenReturn(roomFeature);

        ResponseEntity<?> response = roomFeaturesService.deleteRoomFeature(featureId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roomFeaturesUtils).findById(featureId);
        verify(roomDetailsUtils).save(roomDetails);
        verify(roomFeaturesUtils).delete(roomFeature);
    }
}