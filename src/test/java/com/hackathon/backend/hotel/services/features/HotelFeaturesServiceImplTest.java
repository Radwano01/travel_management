package com.hackathon.backend.hotel.services.features;

import com.hackathon.backend.dto.hotelDto.features.GetHotelFeaturesDto;
import com.hackathon.backend.dto.hotelDto.features.HotelFeatureDto;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.repositories.hotel.RoomDetailsRepository;
import com.hackathon.backend.repositories.hotel.hotelFeatures.HotelFeaturesRepository;
import com.hackathon.backend.services.hotel.hotelFeatures.impl.HotelFeaturesServiceImpl;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelFeaturesServiceImplTest {

    @Mock
    HotelFeaturesRepository hotelFeaturesRepository;

    @Mock
    RoomDetailsRepository roomDetailsRepository;

    @InjectMocks
    HotelFeaturesServiceImpl hotelFeaturesServiceImpl;


    @Test
    void createHotelFeature_Success() {
        // given
        String featureName = "Swimming Pool";
        HotelFeatureDto hotelFeatureDto = new HotelFeatureDto();
        hotelFeatureDto.setHotelFeature(featureName);

        //behavior
        when(hotelFeaturesRepository.existsHotelFeatureByHotelFeatures(featureName)).thenReturn(false);

        // when
        ResponseEntity<String> response = hotelFeaturesServiceImpl.createHotelFeature(hotelFeatureDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hotel feature created successfully Swimming Pool", response.getBody());
    }

    @Test
    void getHotelFeatures() {
        // given
        GetHotelFeaturesDto feature1 = new GetHotelFeaturesDto();
        GetHotelFeaturesDto feature2 = new GetHotelFeaturesDto();
        List<GetHotelFeaturesDto> features = List.of(feature1, feature2);

        //behavior
        when(hotelFeaturesRepository.findAllHotelFeatures()).thenReturn(features);

        // when
        ResponseEntity<List<GetHotelFeaturesDto>> response = hotelFeaturesServiceImpl.getHotelFeatures();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(features, response.getBody());
    }


    @Test
    void editHotelFeature_Success() {
        // given
        int featureId = 1;
        String newFeatureName = "New Feature";
        HotelFeatureDto hotelFeatureDto = new HotelFeatureDto();
        hotelFeatureDto.setHotelFeature(newFeatureName);

        HotelFeaturesEntity existingFeature = new HotelFeaturesEntity();
        existingFeature.setId(featureId);

        //behavior
        when(hotelFeaturesRepository.findById(featureId)).thenReturn(Optional.of(existingFeature));
        when(hotelFeaturesRepository.save(existingFeature)).thenReturn(existingFeature);

        // when
        ResponseEntity<String> response = hotelFeaturesServiceImpl.editHotelFeature(featureId, hotelFeatureDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hotel Feature edited successfully New Feature", response.getBody());

        verify(hotelFeaturesRepository).save(existingFeature);
        assertEquals(newFeatureName, existingFeature.getHotelFeatures());
    }

    @Test
    void deleteHotelFeature_Success() {
        // given
        int featureId = 1;

        HotelFeaturesEntity hotelFeatures = new HotelFeaturesEntity();
        hotelFeatures.setId(featureId);

        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        roomDetails.setHotelFeatures(new ArrayList<>(List.of(hotelFeatures)));
        hotelFeatures.setRoomDetails(new ArrayList<>(List.of(roomDetails)));

        //behavior
        when(hotelFeaturesRepository.findById(featureId)).thenReturn(Optional.of(hotelFeatures));

        // when
        ResponseEntity<String> response = hotelFeaturesServiceImpl.deleteHotelFeature(featureId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hotel feature deleted successfully", response.getBody());

        verify(hotelFeaturesRepository).delete(hotelFeatures);
        verify(roomDetailsRepository).save(roomDetails);
    }
}