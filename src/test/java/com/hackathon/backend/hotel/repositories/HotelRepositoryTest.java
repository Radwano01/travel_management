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
                3,
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

        Pageable pageable = PageRequest.of(1, 6);
        //when
        Page<List<GetHotelDto>> response = hotelRepository.findByCountryId(countryId, pageable);

        //then
        assertEquals(response.getContent().get(0).get(0).getHotelName(), "testName");
        assertEquals(response.getContent().get(0).get(0).getMainImage(), "testImage");
        assertEquals(response.getContent().get(0).get(0).getDescription(), "testDesc");
        assertEquals(response.getContent().get(0).get(0).getAddress(), "testAddress");
        assertEquals(response.getContent().get(0).get(0).getRate(), 3);
    }
}