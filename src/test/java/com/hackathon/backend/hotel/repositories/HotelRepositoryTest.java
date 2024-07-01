package com.hackathon.backend.hotel.repositories;

import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
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
    CountryRepository countryRepository;

    @Autowired
    HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        CountryEntity country = new CountryEntity(
                "testCountry",
                "testImage"
        );
        countryRepository.save(country);

        HotelEntity hotel = new HotelEntity(
                "testName",
                "testImage",
                "testDesc",
                100,
                "testAddress",
                3,
                country
        );
        hotelRepository.save(hotel);
    }

    @AfterEach
    void tearDown() {
        hotelRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    void findByCountryId() {
        // given
        int countryId = countryRepository.findAll().get(0).getId();

        Pageable pageable = PageRequest.of(0, 6); // Zero-based page index

        // when
        Page<GetHotelDto> response = hotelRepository.findByCountryId(countryId, pageable);

        // then
        assertFalse(response.isEmpty(), "Response should not be empty");
        GetHotelDto hotelDto = response.getContent().get(0);
        assertEquals("testName", hotelDto.getHotelName());
        assertEquals("testImage", hotelDto.getMainImage());
        assertEquals("testDesc", hotelDto.getDescription());
        assertEquals("testAddress", hotelDto.getAddress());
        assertEquals(3, hotelDto.getRate());
    }
}
