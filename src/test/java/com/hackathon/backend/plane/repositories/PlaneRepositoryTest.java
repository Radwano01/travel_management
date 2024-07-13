package com.hackathon.backend.plane.repositories;

import com.hackathon.backend.dto.planeDto.GetPlaneDto;
import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlaneRepositoryTest {

    @Autowired
    PlaneRepository planeRepository;

    @BeforeEach
    void setUp(){
        PlaneEntity plane = new PlaneEntity();
        plane.setPlaneCompanyName("test");
        plane.setStatus(true);

        planeRepository.save(plane);
    }

    @AfterEach
    void tearDown(){
        planeRepository.deleteAll();
    }

    @Test
    void findAllPlanes() {

        //when
        List<GetPlaneDto> planeEntities = planeRepository.findAllPlanes();

        //then
        assertEquals("test", planeEntities.get(0).getPlaneCompanyName());
        assertTrue(planeEntities.get(0).isStatus());
    }
}