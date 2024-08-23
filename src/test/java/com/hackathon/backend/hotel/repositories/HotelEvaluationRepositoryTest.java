package com.hackathon.backend.hotel.repositories;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.hotel.HotelEvaluationRepository;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import com.hackathon.backend.repositories.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HotelEvaluationRepositoryTest {

    @Autowired
    private HotelEvaluationRepository hotelEvaluationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    private UserEntity testUser;
    private HotelEntity testHotel;

    @BeforeEach
    void setUp() {
        // Create and save a user
        testUser = new UserEntity();
        testUser.setUsername("testUser");
        testUser.setImage("testUserImage.jpg");
        userRepository.save(testUser);

        // Create and save a hotel
        testHotel = new HotelEntity();
        testHotel.setHotelName("Test Hotel");
        hotelRepository.save(testHotel);

        // Create and save a hotel evaluation
        HotelEvaluationEntity testEvaluation = new HotelEvaluationEntity(
                "test comment",
                5,
                testHotel,
                testUser
        );
        hotelEvaluationRepository.save(testEvaluation);
    }

    @AfterEach
    void tearDown() {
        hotelEvaluationRepository.deleteAll();
        userRepository.deleteAll();
        hotelRepository.deleteAll();
    }

    @Test
    void itShouldReturnHotelEvaluationByUserId() {
        // When
        HotelEvaluationEntity response = hotelEvaluationRepository.findHotelEvaluationByUserId(testUser.getId());

        // Then
        assertNotNull(response);
        assertEquals(testUser.getId(), response.getUser().getId());
        assertEquals(testHotel.getHotelName(), response.getHotel().getHotelName());
        assertEquals("test comment", response.getComment());
        assertEquals(5, response.getRate());
    }
}
