package com.hackathon.backend.plane.repositories;

import com.hackathon.backend.entities.plane.PlaneEntity;
import com.hackathon.backend.entities.plane.PlaneSeatsEntity;
import com.hackathon.backend.repositories.plane.PlaneRepository;
import com.hackathon.backend.repositories.plane.PlaneSeatsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlaneSeatsRepositoryTest {

    @Autowired
    private PlaneRepository planeRepository;

    @Autowired
    private PlaneSeatsRepository planeSeatsRepository;

    @Test
    void testFindAllSeatsByPlaneId() {

        //when
        PlaneEntity plane = new PlaneEntity();
        planeRepository.save(plane);

        PlaneSeatsEntity seat1 = new PlaneSeatsEntity();
        seat1.setPlane(plane);
        planeSeatsRepository.save(seat1);

        PlaneSeatsEntity seat2 = new PlaneSeatsEntity();
        seat2.setPlane(plane);
        planeSeatsRepository.save(seat2);

        //when
        Optional<List<PlaneSeatsEntity>> seatsOptional = planeSeatsRepository.findAllSeatsByPlaneId(plane.getId());

        //then
        assertTrue(seatsOptional.isPresent());
        List<PlaneSeatsEntity> seats = seatsOptional.get();
        assertEquals(2, seats.size());
    }
}