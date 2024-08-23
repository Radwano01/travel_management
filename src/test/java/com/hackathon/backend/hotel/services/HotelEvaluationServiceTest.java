package com.hackathon.backend.hotel.services;

import com.hackathon.backend.dto.hotelDto.evaluationDto.CreateHotelEvaluationDto;
import com.hackathon.backend.dto.hotelDto.evaluationDto.EditHotelEvaluationDto;
import com.hackathon.backend.dto.hotelDto.evaluationDto.GetHotelEvaluationDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import com.hackathon.backend.services.hotel.HotelEvaluationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelEvaluationServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HotelEvaluationService hotelEvaluationService;

    private HotelEntity hotel;

    @BeforeEach
    void setUp() {
        // Initialize UserEntity
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("testUser");

        // Initialize HotelEntity
        hotel = new HotelEntity();
        hotel.setId(1L);
        hotel.setEvaluations(new ArrayList<>());
    }

    @AfterEach
    void tearDown(){
        hotelRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void addComment() {
        // given
        long hotelId = 1L;
        long userId = 1L;
        CreateHotelEvaluationDto createHotelEvaluationDto = new CreateHotelEvaluationDto("Excellent", 5);

        // Initialize entities
        UserEntity user = new UserEntity();
        user.setId(userId);

        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        hotel.setEvaluations(new ArrayList<>());

        // Mock behavior
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        CompletableFuture<ResponseEntity<?>> responseFuture = hotelEvaluationService.addComment(hotelId, userId, createHotelEvaluationDto);
        ResponseEntity<?> response = responseFuture.join();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment added successfully Excellent", response.getBody());
        assertEquals(1, hotel.getEvaluations().size());
        assertEquals("Excellent", hotel.getEvaluations().get(0).getComment());
    }

    @Test
    void addComment_UserAlreadyCommented() {
        // given
        long hotelId = 1L;
        long userId = 1L;
        CreateHotelEvaluationDto createHotelEvaluationDto = new CreateHotelEvaluationDto("Excellent", 5);

        // Initialize UserEntity
        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);

        // Initialize HotelEvaluationEntity
        HotelEvaluationEntity existingEvaluation = new HotelEvaluationEntity();
        existingEvaluation.setUser(existingUser);

        // Initialize HotelEntity
        HotelEntity hotel = new HotelEntity();
        hotel.setId(hotelId);
        hotel.setEvaluations(new ArrayList<>(List.of(existingEvaluation)));

        // behavior
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        // when
        CompletableFuture<ResponseEntity<?>> responseFuture = hotelEvaluationService.addComment(hotelId, userId, createHotelEvaluationDto);
        ResponseEntity<?> response = responseFuture.join();

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("This user has already commented on this hotel", response.getBody());
    }



    @Test
    void getComments() {
        // given
        long hotelId = 1L;

        GetHotelEvaluationDto dto1 = new GetHotelEvaluationDto(1L, "Good", 4, 1L, "user1", "image1");
        GetHotelEvaluationDto dto2 = new GetHotelEvaluationDto(2L, "Excellent", 5, 2L, "user2", "image2");
        List<GetHotelEvaluationDto> mockDtos = List.of(dto1, dto2);

        // behavior
        when(hotelRepository.findAllHotelEvaluationsByHotelId(hotelId)).thenReturn(mockDtos);

        // when
        CompletableFuture<ResponseEntity<?>> responseFuture = hotelEvaluationService.getComments(hotelId);
        ResponseEntity<?> response = responseFuture.join();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockDtos, response.getBody());
    }

    @Test
    void editComment() {
        // given
        long hotelId = 1L;
        long commentId = 1L;
        EditHotelEvaluationDto editHotelEvaluationDto = new EditHotelEvaluationDto();
        editHotelEvaluationDto.setComment("Updated comment");
        editHotelEvaluationDto.setRate(3);

        HotelEvaluationEntity existingEvaluation = new HotelEvaluationEntity();
        existingEvaluation.setId(commentId);
        existingEvaluation.setComment("Old comment");
        existingEvaluation.setRate(1);

        hotel.getEvaluations().add(existingEvaluation);

        // behavior
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        // when
        CompletableFuture<ResponseEntity<?>> responseFuture = hotelEvaluationService.editComment(hotelId, commentId, editHotelEvaluationDto);
        ResponseEntity<?> response = responseFuture.join();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment edited successfullyUpdated comment", response.getBody());
        assertEquals("Updated comment", existingEvaluation.getComment());
        assertEquals(3, existingEvaluation.getRate());
    }

    @Test
    void removeComment() {
        // given
        long hotelId = 1L;
        long commentId = 1L;

        HotelEvaluationEntity commentToRemove = new HotelEvaluationEntity();
        commentToRemove.setId(commentId);
        hotel.setEvaluations(new ArrayList<>(List.of(commentToRemove)));

        // behavior
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        // when
        CompletableFuture<ResponseEntity<?>> responseFuture = hotelEvaluationService.removeComment(hotelId, commentId);
        ResponseEntity<?> response = responseFuture.join();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment deleted successfully", response.getBody());
        assertTrue(hotel.getEvaluations().isEmpty());
    }

    @Test
    void removeComment_HotelNotFound() {
        // given
        long hotelId = 1L;
        long commentId = 1L;

        //behavior
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        //then
        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                hotelEvaluationService.removeComment(hotelId, commentId).join()
        );
        assertEquals("Hotel id not found", exception.getMessage());
    }
}
