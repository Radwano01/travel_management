package com.hackathon.backend.plane.repositories;

import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.repositories.plane.AirPortRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AirPortRepositoryTest {

    @Autowired
    AirPortRepository airPortRepository;

    @Test
    public void testFindAirPortByAirPort() {
        // Given
        AirPortEntity airportEntity = new AirPortEntity("Airport Name", "XYZ", null);
        airPortRepository.save(airportEntity);

        // When
        Optional<AirPortEntity> foundAirport = airPortRepository.findAirPortByAirPortName(airportEntity.getAirPortName());

        // Then
        assertTrue(foundAirport.isPresent());
        assertEquals("Airport Name", foundAirport.get().getAirPortName());
    }
}
