package com.hackathon.backend.country.repositories;

import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceDetailsDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceForFlightDto;
import com.hackathon.backend.dto.hotelDto.GetHotelDto;
import com.hackathon.backend.dto.planeDto.GetAirPortDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.repositories.country.PlaceRepository;
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
import static org.mockito.ArgumentMatchers.anyInt;

@DataJpaTest
class PlaceRepositoryTest {

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    CountryRepository countryRepository;

    private PlaceEntity place;
    private PlaceDetailsEntity placeDetails;

    @BeforeEach
    void setUp() {
        // Create and save a country entity if needed
        CountryEntity country = new CountryEntity();
        country.setCountry("Test Country");
        countryRepository.save(country);
        // Save country if needed

        // Create and save place entity
        place = new PlaceEntity();
        place.setPlace("Test Place");
        place.setMainImage("test-main-image.png");
        place.setCountry(country);

        // Create and save place details entity
        placeDetails = new PlaceDetailsEntity();
        placeDetails.setImageOne("image1.png");
        placeDetails.setImageTwo("image2.png");
        placeDetails.setImageThree("image3.png");
        placeDetails.setDescription("A detailed description of the place.");
        placeDetails.setPlace(place);

        // Set place details in place
        place.setPlaceDetails(placeDetails);

        // Save entities
        placeRepository.save(place);
    }



    @AfterEach
    void tearDown() {
        placeRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    void itShouldReturnExistByPlace() {
        //given
        String placeName = "test-place";

        //when
        List<GetPlaceForFlightDto> response = placeRepository.findPlaceByPlace(placeName);

        //then
        assertNotNull(response);
    }

    @Test
    void itShouldReturnNotFoundByPlace() {
        //given
        String placeName = "place";

        //when
        List<GetPlaceForFlightDto> response = placeRepository.findPlaceByPlace(placeName);

        //then
        assertTrue(response.isEmpty());
    }

    @Test
    void itShouldReturnPlaceWithPlaceDetailsByPlaceId() {
        // when
        GetPlaceDetailsDto response = placeRepository.findPlaceWithPlaceDetailsByPlaceId(place.getId());

        // then
        assertNotNull(response, "Response should not be null");
        assertEquals(place.getId(), response.getId(), "Place ID should match");
        assertEquals(place.getPlace(), response.getPlace(), "Place name should match");
        assertEquals(place.getMainImage(), response.getMainImage(), "Main image should match");
        assertEquals(placeDetails.getImageOne(), response.getImageOne(), "Image one should match");
        assertEquals(placeDetails.getImageTwo(), response.getImageTwo(), "Image two should match");
        assertEquals(placeDetails.getImageThree(), response.getImageThree(), "Image three should match");
        assertEquals(placeDetails.getDescription(), response.getDescription(), "Description should match");
    }

    @Test
    void itShouldReturnHotelByPlaceId() {
        //given
        Pageable pageable = PageRequest.of(0, 6);

        //when
        Page<GetHotelDto> response = placeRepository.findHotelByPlaceId(place.getId(), pageable);

        //then
        assertNotNull(response);
    }

    @Test
    void itShouldReturnAllAirportsByPlaceId() {

        // when
        List<GetAirPortDto> response = placeRepository.findAllAirportsByPlaceId(place.getId());

        // then
        assertNotNull(response);
    }

}