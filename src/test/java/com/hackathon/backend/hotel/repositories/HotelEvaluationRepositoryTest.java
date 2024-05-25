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
    HotelRepository hotelRepository;

    @Autowired
    HotelEvaluationRepository hotelEvaluationRepository;

    @Autowired
    UserRepository userRepository;

    long userId;

    @BeforeEach
    public void setUp() {
        HotelEntity hotel = new HotelEntity();
        hotelRepository.save(hotel);

        UserEntity user = new UserEntity();
        userRepository.save(user);
        userId = user.getId();

        HotelEvaluationEntity evaluation = new HotelEvaluationEntity();
        evaluation.setHotel(hotel);
        evaluation.setUser(user);
        evaluation.setComment("testComment");
        evaluation.setRate(2.40f);

        hotel.getEvaluations().add(evaluation);

        hotelEvaluationRepository.save(evaluation);
        hotelRepository.save(hotel);
    }

    @AfterEach
    void tearDown(){
        hotelEvaluationRepository.deleteAll();
        hotelRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testExistsCommentByUserId() {

        //when
        boolean response = hotelEvaluationRepository.existsCommentByUserId(userId);

        //then
        assertTrue(response);
    }
}