package com.hackathon.backend.hotel.services.features;

import com.hackathon.backend.dto.hotelDto.features.RoomFeatureDto;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.repositories.hotel.hotelFeatures.RoomFeaturesRepository;
import com.hackathon.backend.services.hotel.hotelFeatures.RoomFeaturesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomFeaturesServiceTest {

    @Mock
    private RoomFeaturesRepository roomFeaturesRepository;

    @InjectMocks
    private RoomFeaturesService roomFeaturesService;


    @Test
    void createRoomFeature_Success() {
        // given
        RoomFeatureDto roomFeatureDto = new RoomFeatureDto();
        roomFeatureDto.setRoomFeature("Free WiFi");

        //behavior
        when(roomFeaturesRepository.existsRoomFeatureByRoomFeatures(roomFeatureDto.getRoomFeature().trim())).thenReturn(false);

        // when
        ResponseEntity<String> response = roomFeaturesService.createRoomFeature(roomFeatureDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Room feature created successfully Free WiFi", response.getBody());

        verify(roomFeaturesRepository, times(1)).save(any(RoomFeaturesEntity.class));
    }

    @Test
    void createRoomFeature_FeatureAlreadyExists() {
        // given
        RoomFeatureDto roomFeatureDto = new RoomFeatureDto();
        roomFeatureDto.setRoomFeature("Free WiFi");

        //behavior
        when(roomFeaturesRepository.existsRoomFeatureByRoomFeatures(roomFeatureDto.getRoomFeature().trim())).thenReturn(true);

        // when
        ResponseEntity<String> response = roomFeaturesService.createRoomFeature(roomFeatureDto);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Hotel Feature already exists", response.getBody());

        verify(roomFeaturesRepository, never()).save(any(RoomFeaturesEntity.class));
    }

    @Test
    void getRoomFeatures() {
        // given
        RoomFeaturesEntity feature1 = new RoomFeaturesEntity("Free WiFi");
        RoomFeaturesEntity feature2 = new RoomFeaturesEntity("Air Conditioning");
        List<RoomFeaturesEntity> features = new ArrayList<>(List.of(feature1, feature2));

        // behavior
        when(roomFeaturesRepository.findAll()).thenReturn(features);

        // when
        ResponseEntity<List<RoomFeaturesEntity>> response = roomFeaturesService.getRoomFeatures();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(features, response.getBody());
    }

    @Test
    void editRoomFeature_Success() {
        // given
        int featureId = 1;
        RoomFeatureDto roomFeatureDto = new RoomFeatureDto();
        roomFeatureDto.setRoomFeature("Updated Feature");
        RoomFeaturesEntity existingFeature = new RoomFeaturesEntity("Old Feature");
        existingFeature.setId(featureId);

        // behavior
        when(roomFeaturesRepository.findById(featureId)).thenReturn(Optional.of(existingFeature));

        // when
        ResponseEntity<String> response = roomFeaturesService.editRoomFeature(featureId, roomFeatureDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Room Feature edited successfully Updated Feature", response.getBody());

        verify(roomFeaturesRepository, times(1)).save(existingFeature);
    }

    @Test
    void deleteRoomFeature_Success() {
        // given
        int featureId = 1;
        RoomFeaturesEntity roomFeatures = new RoomFeaturesEntity("Feature to delete");
        roomFeatures.setId(featureId);

        //behavior
        when(roomFeaturesRepository.findById(featureId)).thenReturn(Optional.of(roomFeatures));

        // when
        ResponseEntity<String> response = roomFeaturesService.deleteRoomFeature(featureId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Room feature deleted successfully", response.getBody());

        verify(roomFeaturesRepository, times(1)).delete(roomFeatures);
    }
}
