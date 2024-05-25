package com.hackathon.backend.hotel.services.features;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.services.hotel.hotelFeatures.HotelFeaturesRelationsService;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.features.HotelFeaturesUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class HotelFeaturesRelationsServiceTest {

    @Mock
    HotelUtils hotelUtils;

    @Mock
    HotelFeaturesUtils hotelFeaturesUtils;

    @InjectMocks
    HotelFeaturesRelationsService hotelFeaturesRelationsService;

    @Test
    void addHotelFeatureToHotel() {
        //given
        long hotelId = 1;
        int featureId = 1;

        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);

        RoomDetailsEntity roomDetails = new RoomDetailsEntity();

        hotel.setRoomDetails(roomDetails);

        HotelFeaturesEntity hotelFeature = new HotelFeaturesEntity();
        hotelFeature.setId(featureId);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        when(hotelFeaturesUtils.findById(featureId)).thenReturn(hotelFeature);

        //when
        ResponseEntity<?> response = hotelFeaturesRelationsService.addHotelFeatureToHotel(hotelId, featureId);


        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(hotelUtils).findHotelById(hotelId);
        verify(hotelFeaturesUtils).findById(featureId);
        verify(hotelUtils).save(hotel);
        verify(hotelFeaturesUtils).save(hotelFeature);
    }

    @Test
    void removeHotelFeatureFromHotel() {
        //given
        long hotelId = 1;
        int featureId = 1;

        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        hotel.setRoomDetails(new RoomDetailsEntity());

        HotelFeaturesEntity hotelFeature = new HotelFeaturesEntity();
        hotelFeature.setId(featureId);

        hotel.getRoomDetails().getHotelFeatures().add(hotelFeature);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        when(hotelFeaturesUtils.findById(featureId)).thenReturn(hotelFeature);

        //when
        ResponseEntity<?> response = hotelFeaturesRelationsService.removeHotelFeatureFromHotel(hotelId, featureId);


        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(hotelUtils).findHotelById(hotelId);
        verify(hotelFeaturesUtils).findById(featureId);
        verify(hotelUtils).save(hotel);
        verify(hotelFeaturesUtils).save(hotelFeature);
    }
}