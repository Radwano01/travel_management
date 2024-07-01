package com.hackathon.backend.plane.repositories;

import com.hackathon.backend.dto.planeDto.GetAirPortDto;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.repositories.plane.AirPortRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AirPortRepositoryTest {

    @Autowired
    AirPortRepository airPortRepository;

    @Autowired
    PlaceRepository placeRepository;

    @BeforeEach
    void setUp(){
        PlaceEntity place = new PlaceEntity();
        placeRepository.save(place);


        AirPortEntity airport1 = new AirPortEntity();
        airport1.setAirPortName("Airport 1");
        airport1.setAirPortCode("AP1");
        airport1.setPlace(place);

        AirPortEntity airport2 = new AirPortEntity();
        airport2.setAirPortName("Airport 2");
        airport2.setAirPortCode("AP2");
        airport2.setPlace(place);

        airPortRepository.save(airport1);
        airPortRepository.save(airport2);
    }

    @AfterEach
    void tearDown(){
        airPortRepository.deleteAll();
        placeRepository.deleteAll();
    }

    @Test
    void FindAirPortByAirPort() {

        // when
        Optional<AirPortEntity> foundAirport = airPortRepository.findAirPortByAirPortName("Airport 1");

        // then
        assertTrue(foundAirport.isPresent());
        assertEquals("Airport 1", foundAirport.get().getAirPortName());
    }

    @Test
    void existsAirPortByAirPortCode(){
        boolean exists = airPortRepository.existsAirPortByAirPortCode("AP2");
        assertTrue(exists);
    }

    @Test
    void findPlaceByPlace(){

        //when
        Optional<List<GetAirPortDto>> result = airPortRepository.findByPlaceId(1);

        //then
        assertNotNull(result.get());
        assertEquals(result.get().size(),2);
        assertEquals(result.get().get(0).getAirPortName(), "Airport 1");
        assertEquals(result.get().get(1).getAirPortName(), "Airport 2");;
    }
}
