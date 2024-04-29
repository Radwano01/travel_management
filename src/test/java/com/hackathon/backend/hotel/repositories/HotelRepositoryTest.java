package com.hackathon.backend.hotel.repositories;

import com.hackathon.backend.dto.hotelDto.HotelDto;
import com.hackathon.backend.repositories.hotel.HotelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    void findByCountryId() {
        //given
        int countryId = 1;
        //when
        List<HotelDto> queryCountryDto = hotelRepository.findByCountryId(countryId);
        //then
        assertNotNull(queryCountryDto);
    }
}