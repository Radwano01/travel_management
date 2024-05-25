package com.hackathon.backend.hotel.repositories;

import com.hackathon.backend.dto.hotelDto.HotelDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HotelRepositoryTest {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    HotelRepository hotelRepository;

    @BeforeEach
    void setUp(){
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
                country
        );
        hotelRepository.save(hotel);
    }

    @AfterEach
    void tearDown(){
        hotelRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    void findByCountryId() {
        //given
        int countryId = countryRepository.findAll().get(0).getId();

        //when
        List<HotelDto> response = hotelRepository.findByCountryId(countryId);

        //then
        assertEquals(response.get(0).getHotelName(), "testName");
        assertEquals(response.get(0).getMainImage(), "testImage");
        assertEquals(response.get(0).getDescription(), "testDesc");
        assertEquals(response.get(0).getAddress(), "testAddress");
        assertEquals(response.get(0).getRate(), 0f);
    }
}