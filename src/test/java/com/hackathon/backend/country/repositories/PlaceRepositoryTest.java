package com.hackathon.backend.country.repositories;

import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.repositories.country.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class PlaceRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository;

    @BeforeEach
    void setUp() {
        String place = "London";
        PlaceEntity placeEntity = new PlaceEntity(place);
        placeRepository.save(placeEntity);
    }

    @AfterEach
    void tearDown() {
        placeRepository.deleteAll();
    }

    @Test
    void findByPlace() {
        //given
        String place = "London";
        //when
        PlaceEntity placeEntity = placeRepository.findByPlace(place)
                .orElseThrow(()-> new EntityNotFoundException("place not found"));
        //then
        assertEquals(placeEntity.getPlace(), place);

    }

    @Test
    void findAllPlacesByCountryId(){
        //given
        int countryId = 1;

        //when
        List<PlaceEntity> places = placeRepository.findAllPlacesByCountryId(countryId);

        //then
        assertNotNull(places);
    }

    @Test
    void deleteByCountryIdAndId(){
        //given
        int countryId = 1;
        int placeId = 1;
        //when & then
        try{
            placeRepository.deletePlaceByCountryIdAndId(countryId, placeId);
            assertTrue(true);
        }catch(Exception e){
            fail("Deletion failed: " + e.getMessage());
        }
    }
}