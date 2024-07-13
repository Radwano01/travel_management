package com.hackathon.backend.hotel.repositories;

import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HotelRepositoryTest {

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        PlaceEntity place = new PlaceEntity();
        place.setPlace("testPlace");
        place.setMainImage("testImage");

        placeRepository.save(place);

        HotelEntity hotel = new HotelEntity(
                "testName",
                "testImage",
                "testDesc",
                100,
                "testAddress",
                3,
                place
        );
        hotelRepository.save(hotel);
    }

    @AfterEach
    void tearDown() {
        hotelRepository.deleteAll();
        placeRepository.deleteAll();
    }

    @Test
    void findByPlaceId() {
        // given
        int placeId = placeRepository.findAll().get(0).getId();

        Pageable pageable = PageRequest.of(0, 6); // Zero-based page index

        // when
        List<GetHotelDto> response = hotelRepository.findByPlaceId(placeId, pageable);

        // then
        assertFalse(response.isEmpty(), "Response should not be empty");
        GetHotelDto hotelDto = response.get(0);
        assertEquals("testName", hotelDto.getHotelName());
        assertEquals("testImage", hotelDto.getMainImage());
        assertEquals("testDesc", hotelDto.getDescription());
        assertEquals("testAddress", hotelDto.getAddress());
        assertEquals(3, hotelDto.getRate());
    }
}
