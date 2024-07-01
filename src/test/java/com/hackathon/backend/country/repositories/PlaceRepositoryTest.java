package com.hackathon.backend.country.repositories;

import com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceForFlightDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.plane.AirPortEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.repositories.plane.AirPortRepository;
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
    CountryRepository countryRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    AirPortRepository airPortRepository;

    @BeforeEach
    void setUp() {
        CountryEntity country = new CountryEntity(
                "testCountry",
                "testImage"
        );
        countryRepository.save(country);

        PlaceEntity place = new PlaceEntity(
                "testPlace",
                "testImage",
                country
        );
        placeRepository.save(place);
    }

    @AfterEach
    void tearDown() {
        placeRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    void findPlacesByCountryId() {
        //given
        int countryId = countryRepository.findAll().get(0).getId();

        //when
        List<GetEssentialPlaceDto> places = placeRepository.findPlacesByCountryId(countryId);

        //then
        assertEquals(places.get(0).getPlace(), "testPlace");
        assertEquals(places.get(0).getMainImage(), "testImage");
    }

    @Test
    void findPlaceByPlace(){
        // //given
        PlaceEntity place = new PlaceEntity();
        place.setPlace("Test Place");

        AirPortEntity airport1 = new AirPortEntity();
        airport1.setAirPortName("Airport 1");
        airport1.setPlace(place);

        AirPortEntity airport2 = new AirPortEntity();
        airport2.setAirPortName("Airport 2");
        airport2.setPlace(place);

        placeRepository.save(place);
        airPortRepository.save(airport1);
        airPortRepository.save(airport2);


        //when
        List<GetPlaceForFlightDto> result = placeRepository.findPlaceByPlace("Test Place");

        //then
        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getPlace(), "Test Place");
        assertEquals(result.get(0).getAirPortName(), "Airport 1");
        assertEquals(result.get(1).getPlace(), "Test Place");
        assertEquals(result.get(1).getAirPortName(), "Airport 2");
    }
}