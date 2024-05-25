package com.hackathon.backend.hotel.services.features;

import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.services.hotel.hotelFeatures.HotelFeaturesService;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.features.HotelFeaturesUtils;
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
class HotelFeaturesServiceTest {

    @Mock
    HotelFeaturesUtils hotelFeaturesUtils;

    @Mock
    RoomDetailsUtils roomDetailsUtils;

    @InjectMocks
    HotelFeaturesService hotelFeaturesService;

    @Test
    void createHotelFeature() {
        //given
        String hotelFeature = "testFeature";

        //behavior
        when(hotelFeaturesUtils.existsHotelFeatureByHotelFeatures(hotelFeature)).thenReturn(false);

        //when
        ResponseEntity<?> response = hotelFeaturesService.createHotelFeature(hotelFeature);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getHotelFeatures() {
        //given
        List<HotelFeaturesEntity> hotelFeatures = new ArrayList<>();

        //behavior
        when(hotelFeaturesUtils.findAll()).thenReturn(hotelFeatures);

        //when
        ResponseEntity<?> response = hotelFeaturesService.getHotelFeatures();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(hotelFeatures, response.getBody());
    }

    @Test
    void editHotelFeature() {
        //given
        int featureId = 1;
        String newHotelFeature = "testFeature";
        HotelFeaturesEntity hotelFeatureEntity = new HotelFeaturesEntity();

        //behavior
        when(hotelFeaturesUtils.findById(featureId)).thenReturn(hotelFeatureEntity);

        //when
        ResponseEntity<?> response = hotelFeaturesService.editHotelFeature(featureId, newHotelFeature);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newHotelFeature, hotelFeatureEntity.getHotelFeatures());
    }

    @Test
    void deleteHotelFeature() {
        //given
        int featureId = 1;

        HotelFeaturesEntity hotelFeaturesEntity = new HotelFeaturesEntity();
        hotelFeaturesEntity.setId(featureId);
        hotelFeaturesEntity.setHotelFeatures("testFeature");

        RoomDetailsEntity roomDetailsEntity = new RoomDetailsEntity();
        roomDetailsEntity.setHotelFeatures(List.of(hotelFeaturesEntity));

        //behavior
        when(hotelFeaturesUtils.findById(featureId)).thenReturn(hotelFeaturesEntity);

        ResponseEntity<?> response = hotelFeaturesService.deleteHotelFeature(featureId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(hotelFeaturesUtils).delete(hotelFeaturesEntity);
    }
}