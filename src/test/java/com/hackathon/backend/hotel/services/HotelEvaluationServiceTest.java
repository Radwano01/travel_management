package com.hackathon.backend.hotel.services;

import com.hackathon.backend.dto.hotelDto.HotelEvaluationDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.services.hotel.HotelEvaluationService;
import com.hackathon.backend.utilities.UserUtils;
import com.hackathon.backend.utilities.hotel.HotelEvaluationUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
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
class HotelEvaluationServiceTest {

    @Mock
    HotelUtils hotelUtils;

    @Mock
    UserUtils userUtils;

    @Mock
    HotelEvaluationUtils hotelEvaluationUtils;

    @InjectMocks
    HotelEvaluationService hotelEvaluationService;

    @Test
    void addComment() {
        //given
        long hotelId = 1L;
        long userId = 1L;
        HotelEvaluationDto hotelEvaluationDto = new HotelEvaluationDto();
        hotelEvaluationDto.setComment("testComment");
        hotelEvaluationDto.setRate(4.5f);

        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        UserEntity user = new UserEntity();
        user.setId(userId);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        when(userUtils.findById(userId)).thenReturn(user);
        when(hotelEvaluationUtils.existsCommentByUserId(userId)).thenReturn(false);

        //when
        ResponseEntity<?> response = hotelEvaluationService.addComment(hotelId, userId, hotelEvaluationDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getComments() {
        //given
        long hotelId = 1;

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("testUsername");
        user.setImage("testImage");


        HotelEvaluationEntity hotelEvaluation = new HotelEvaluationEntity();
        hotelEvaluation.setComment("testComment");
        hotelEvaluation.setRate(4.0f);
        hotelEvaluation.setUser(user);

        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        hotel.getEvaluations().add(hotelEvaluation);

        hotelEvaluation.setHotel(hotel);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);

        //when
        ResponseEntity<?> response = hotelEvaluationService.getComments(hotelId);
        List<HotelEvaluationDto> responseData = (List<HotelEvaluationDto>) response.getBody();
        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(responseData);
        assertEquals(hotelEvaluation.getComment(), responseData.get(0).getComment());
        assertEquals(hotelEvaluation.getRate(), responseData.get(0).getRate());
        assertEquals(hotelEvaluation.getUser().getUsername(), user.getUsername());
        assertEquals(hotelEvaluation.getUser().getImage(), user.getImage());
    }

    @Test
    void editComment() {
        //given
        long commentId = 1L;
        HotelEvaluationDto hotelEvaluationDto = new HotelEvaluationDto();
        hotelEvaluationDto.setComment("testComment");
        hotelEvaluationDto.setRate(2.5f);
        HotelEvaluationEntity hotelEvaluation = new HotelEvaluationEntity("testComment1", 3.5f,
                new HotelEntity(), new UserEntity());

        //behavior
        when(hotelEvaluationUtils.findById(commentId)).thenReturn(hotelEvaluation);

        //when
        ResponseEntity<?> response = hotelEvaluationService.editComment(commentId, hotelEvaluationDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(hotelEvaluationDto.getComment(), hotelEvaluation.getComment());
        assertEquals(hotelEvaluationDto.getRate(), hotelEvaluation.getRate());
        verify(hotelEvaluationUtils).save(hotelEvaluation);
    }

    @Test
    void removeComment() {
        //given
        long hotelId = 1L;
        long userId = 1L;
        long commentId = 1L;

        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);

        UserEntity user = new UserEntity();
        user.setId(userId);

        HotelEvaluationEntity hotelEvaluation = new HotelEvaluationEntity("testComment", 4.0f, hotel, user);

        //behavior
        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotel);
        when(userUtils.findById(userId)).thenReturn(user);
        when(hotelEvaluationUtils.findById(commentId)).thenReturn(hotelEvaluation);

        //when
        ResponseEntity<?> response = hotelEvaluationService.removeComment(hotelId, userId, commentId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(hotelUtils).save(hotel);
        verify(userUtils).save(user);
        verify(hotelEvaluationUtils).delete(hotelEvaluation);
    }
}