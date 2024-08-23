package com.hackathon.backend.plane.repositories;

import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.dto.planeDto.GetPlaneDto;
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
    void setUp() {
        // Create and save sample planes
        PlaneEntity plane1 = new PlaneEntity();
        plane1.setPlaneCompanyName("Airways");
        plane1.setNumSeats(150);
        plane1.setPaidSeats(100);
        plane1.setStatus(true);
        planeRepository.save(plane1);

        PlaneEntity plane2 = new PlaneEntity();
        plane2.setPlaneCompanyName("SkyJets");
        plane2.setNumSeats(200);
        plane2.setPaidSeats(180);
        plane2.setStatus(true);
        planeRepository.save(plane2);
    }

    @AfterEach
    void tearDown() {
        planeRepository.deleteAll();
    }

    @Test
    void findAllPlanes() {


        //when
        List<GetPlaneDto> planes = planeRepository.findAllPlanes();

        //then
        assertNotNull(planes);
        assertEquals(2, planes.size());

        GetPlaneDto plane1 = planes.get(0);
        assertEquals("Airways", plane1.getPlaneCompanyName());
        assertEquals(150, plane1.getNumSeats());
        assertEquals(100, plane1.getPaidSeats());
        assertTrue(plane1.isStatus());

        GetPlaneDto plane2 = planes.get(1);
        assertEquals("SkyJets", plane2.getPlaneCompanyName());
        assertEquals(200, plane2.getNumSeats());
        assertEquals(180, plane2.getPaidSeats());
        assertTrue(plane2.isStatus());
    }
}
