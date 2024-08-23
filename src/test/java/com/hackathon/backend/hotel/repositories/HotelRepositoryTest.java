package com.hackathon.backend.hotel.repositories;

import com.hackathon.backend.dto.hotelDto.evaluationDto.GetHotelEvaluationDto;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HotelRepositoryTest {

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    UserRepository userRepository;


    @BeforeEach
    void setUp() {
        // Create a user
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setImage("testUserImage.jpg");
        userRepository.save(user);

        // Create a hotel
        HotelEntity hotel = new HotelEntity();
        hotel.setHotelName("Test Hotel");

        // Create a hotel evaluation
        HotelEvaluationEntity hotelEvaluation = new HotelEvaluationEntity(
                "test comment",
                5,
                hotel,
                user
        );

        // Add evaluation to hotel
        hotel.getEvaluations().add(hotelEvaluation);

        // Save hotel entity
        hotelRepository.save(hotel);
    }

    @AfterEach
    void tearDown() {
        hotelRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void itShouldReturnAllHotelEvaluationsByHotelId() {
        //given
        long hotelId = hotelRepository.findAll().get(0).getId();

        //when
        List<GetHotelEvaluationDto> evaluations = hotelRepository.findAllHotelEvaluationsByHotelId(hotelId);

        //then
        assertEquals(1, evaluations.size());
        GetHotelEvaluationDto evaluation = evaluations.get(0);
        assertEquals("test comment", evaluation.getComment());
        assertEquals(5, evaluation.getRate());
        assertEquals("testUser", evaluation.getUsername());
        assertEquals("testUserImage.jpg", evaluation.getUserImage());
    }
}