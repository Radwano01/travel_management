package com.hackathon.backend.hotel.services.features;

import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.services.hotel.featuresSerivces.HotelFeaturesService;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.features.HotelFeaturesUtils;
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
class HotelFeaturesServiceTest {

    @Mock
    private RoomDetailsUtils roomDetailsUtils;

    @Mock
    private HotelFeaturesUtils hotelFeaturesUtils;

    private HotelFeaturesService hotelFeaturesService;

    @BeforeEach
    void setUp() {
        hotelFeaturesService = new HotelFeaturesService(roomDetailsUtils,
                hotelFeaturesUtils);
    }

    @AfterEach
    void tearDown() {
        roomDetailsUtils.deleteAll();
        hotelFeaturesUtils.deleteAll();
    }

    @Test
    void createHotelFeature() {
        //given
        String feature = "test hotel feature";
        //when
        ResponseEntity<?> response = hotelFeaturesService.createHotelFeature(feature);
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editHotelFeature() {
        //given
        int featureId = 1;
        String feature = "edited test hotel feature";

        when(hotelFeaturesUtils.findById(featureId))
                .thenReturn(new HotelFeaturesEntity());
        //when
        ResponseEntity<?> response = hotelFeaturesService.editHotelFeature(featureId,feature);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void deleteHotelFeature() {
        //given
        int featureId = 1;

        when(hotelFeaturesUtils.findById(featureId)).thenReturn(new HotelFeaturesEntity());
        //when
        ResponseEntity<?> response = hotelFeaturesService.deleteHotelFeature(featureId);
        //then
        assertEquals(HttpStatus.OK ,response.getStatusCode());
    }
}