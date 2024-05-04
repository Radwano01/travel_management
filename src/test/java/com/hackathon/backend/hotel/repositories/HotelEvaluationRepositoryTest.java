package com.hackathon.backend.hotel.repositories;

import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.user.UserEntity;
import com.hackathon.backend.repositories.hotel.HotelEvaluationRepository;

import com.hackathon.backend.repositories.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class HotelEvaluationRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelEvaluationRepository hotelEvaluationRepository;

    @Test
    public void testExistsCommentByUserId() {
        //given
        HotelEvaluationEntity evaluation = new HotelEvaluationEntity();
        UserEntity user = new UserEntity();
        user.setId(1L);
        userRepository.save(user);
        evaluation.setUser(user);
        evaluation.setComment("Nice hotel!");
        hotelEvaluationRepository.save(evaluation);

        //when
        boolean commentExists = hotelEvaluationRepository.existsCommentByUserId(1L);

        //then
        assertTrue(commentExists);
    }
}