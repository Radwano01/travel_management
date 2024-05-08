package com.hackathon.backend.country.repositories;

import com.hackathon.backend.dto.countryDto.placeDto.EssentialPlaceDto;
import com.hackathon.backend.repositories.country.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlaceRepositoryTest {

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    void findPlacesByCountryId() {
        //given
        int countryId = 1;

        //when
        List<EssentialPlaceDto> placeDtos = placeRepository.findPlacesByCountryId(countryId);

        //then
        assertNotNull(placeDtos);
    }
}