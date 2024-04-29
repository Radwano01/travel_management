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
    private HotelRepository hotelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HotelEvaluationRepository hotelEvaluationRepository;

    @BeforeEach
    void setUp(){
        HotelEntity hotel = new HotelEntity();
        hotel.setId(1L);
        hotelRepository.save(hotel);

        UserEntity user = new UserEntity();
        user.setId(1L);
        userRepository.save(user);

        HotelEvaluationEntity hotelEvaluation = new HotelEvaluationEntity();
        hotelEvaluation.setId(1L);
        hotelEvaluation.setComment("test");
        hotelEvaluation.setUser(user);
        hotelEvaluation.setHotel(hotel);
        hotelEvaluationRepository.save(hotelEvaluation);
    }

    @AfterEach
    void tearDown(){
        hotelEvaluationRepository.deleteAll();
        hotelRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findCommentByUserId() {
        //given
        long userId = 1L;

        //when
        boolean existsUser = hotelEvaluationRepository.existsCommentByUserId(userId);
        //then
        assertTrue(existsUser);
    }
}