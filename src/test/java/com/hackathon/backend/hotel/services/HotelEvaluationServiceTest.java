//package com.hackathon.backend.hotel.services;
//
//import com.hackathon.backend.dto.hotelDto.HotelEvaluationDto;
//import com.hackathon.backend.entities.hotel.HotelEntity;
//import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
//import com.hackathon.backend.entities.user.UserEntity;
//import com.hackathon.backend.repositories.hotel.HotelEvaluationRepository;
//import com.hackathon.backend.repositories.hotel.HotelRepository;
//import com.hackathon.backend.repositories.user.UserRepository;
//import com.hackathon.backend.services.hotel.HotelEvaluationService;
//import com.hackathon.backend.utilities.hotel.HotelUtils;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class HotelEvaluationServiceTest {
//
//    @Mock
//    private HotelRepository hotelRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private HotelEvaluationRepository hotelEvaluationRepository;
//
//    @Mock
//    private HotelUtils hotelUtils;
//
//    private HotelEvaluationService hotelEvaluationService;
//
//    @BeforeEach
//    void setUp() {
//        hotelEvaluationService = new HotelEvaluationService(hotelRepository,
//                userRepository, hotelEvaluationRepository,hotelUtils);
//    }
//
//    @AfterEach
//    void tearDown() {
//        hotelRepository.deleteAll();
//        userRepository.deleteAll();
//        hotelEvaluationRepository.deleteAll();
//    }
//
//    @Test
//    void addComment() {
//        //given
//        long hotelId = 1L;
//        long userId = 1L;
//        HotelEvaluationDto hotelEvaluationDto = new HotelEvaluationDto();
//        hotelEvaluationDto.setComment("test comment");
//        hotelEvaluationDto.setRate(3.40f);
//        when(hotelUtils.findHotelById(hotelId)).thenReturn(new HotelEntity());
//        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
//
//        //when
//        ResponseEntity<?> response = hotelEvaluationService.addComment(hotelId,userId,hotelEvaluationDto);
//
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void getComments() {
//        //given
//        long hotelId = 1L;
//        HotelEntity hotelEntity = new HotelEntity();
//        hotelEntity.setId(hotelId);
//
//        HotelEvaluationEntity evaluationEntity = new HotelEvaluationEntity();
//        evaluationEntity.setId(1L);
//        evaluationEntity.setComment("Test comment");
//        evaluationEntity.setRate(5);
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername("testuser");
//        userEntity.setImage("url_image");
//        evaluationEntity.setUser(userEntity);
//        hotelEntity.getEvaluations().add(evaluationEntity);
//
//        when(hotelUtils.findHotelById(hotelId)).thenReturn(hotelEntity);
//        //when
//        ResponseEntity<?> response = hotelEvaluationService.getComments(hotelId);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        List<HotelEvaluationDto> hotelEvaluationDtoList = (List<HotelEvaluationDto>) response.getBody();
//        assertNotNull(hotelEvaluationDtoList);
//        HotelEvaluationDto hotelEvaluationDto = hotelEvaluationDtoList.get(0);
//        assertEquals(evaluationEntity.getId(), hotelEvaluationDto.getId());
//        assertEquals(evaluationEntity.getComment(), hotelEvaluationDto.getComment());
//        assertEquals(evaluationEntity.getRate(), hotelEvaluationDto.getRate());
//        assertEquals(evaluationEntity.getUser().getUsername(), hotelEvaluationDto.getUsername());
//        assertEquals(evaluationEntity.getUser().getImage(), hotelEvaluationDto.getUserImage());
//
//    }
//
//    @Test
//    void editComment() {
//        //given
//        long commentId = 1L;
//
//        HotelEvaluationDto hotelEvaluationDto = new HotelEvaluationDto();
//        hotelEvaluationDto.setComment("edit test comment");
//        hotelEvaluationDto.setRate(1.99f);
//
//        when(hotelEvaluationRepository.findById(commentId)).thenReturn(Optional.of(new HotelEvaluationEntity()));
//        //when
//        ResponseEntity<?> response = hotelEvaluationService.editComment(commentId,hotelEvaluationDto);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//    }
//
//    @Test
//    void deleteComment() {
//
//        long hotelId = 1L;
//        long userId = 1L;
//        long commentId = 1L;
//
//        when(hotelUtils.findHotelById(hotelId)).thenReturn(new HotelEntity());
//        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));
//        when(hotelEvaluationRepository.findById(commentId)).thenReturn(Optional.of(new HotelEvaluationEntity()));
//        //when
//        ResponseEntity<?> response = hotelEvaluationService.deleteComment(hotelId,userId,commentId);
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//    }
//}