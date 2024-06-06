package com.hackathon.backend.country.services;
import com.hackathon.backend.dto.countryDto.CountryDto;
import com.hackathon.backend.dto.countryDto.PostC;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.services.country.CountryService;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryDetailsUtils;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.package_.PackageUtils;
import com.hackathon.backend.utilities.plane.PlaneFlightsUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    CountryUtils countryUtils;

    @Mock
    CountryDetailsUtils countryDetailsUtils;

    @Mock
    PlaceUtils placeUtils;

    @Mock
    HotelUtils hotelUtils;

    @Mock
    PackageUtils packageUtils;

    @Mock
    PlaneFlightsUtils planeFlightsUtils;

    @Mock
    S3Service s3Service;

    @InjectMocks
    CountryService countryService;

    @Test
    void createCountry() {
        //given
        PostC c = new PostC(
                "testCountry",
                new MockMultipartFile("mainImage", "mainImage.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageOne", "imageOne.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageTwo", "imageTwo.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageThree", "imageThree.jpg", "image/jpeg", new byte[0]),
                "testDesc"
        );

        // Assuming countryService.createCountry accepts a PostC object
        // when
        ResponseEntity<?> response = countryService.createCountry(c);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getCountry() {
        //given
        CountryDto countryDto = new CountryDto();
        countryDto.setCountry("testCountry");
        countryDto.setMainImage("testImage");
        List<CountryDto> countryDtos = new ArrayList<>();

        //behavior
        when(countryUtils.findAllCountries()).thenReturn(countryDtos);

        //when
        ResponseEntity<?> response = countryService.getCountry();

        ///then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(countryDtos, response.getBody());
    }

    @Test
    void editCountry() {
        //given
        int countryId = 1;
        CountryEntity country = new CountryEntity();
        country.setId(countryId);
        country.setCountry("testCountry");
        country.setMainImage("testImage");

        CountryDto countryDto = new CountryDto();
        countryDto.setCountry("testCountry1");
        countryDto.setMainImage("testImage1");

        //behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);

        //when
        ResponseEntity<?> response = countryService.editCountry(countryId, countryDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(countryDto.getCountry(), country.getCountry());
        assertEquals(countryDto.getMainImage(), country.getMainImage());
    }

    @Test
    void deleteCountry() {
        //given
        int countryId = 1;
        CountryEntity country = new CountryEntity();
        country.setId(countryId);

        PlaceEntity place = new PlaceEntity();
        HotelEntity hotel = new HotelEntity();
        PackageEntity packageEntity = new PackageEntity();
        PlaneFlightsEntity departingFlight = new PlaneFlightsEntity();
        PlaneFlightsEntity arrivingFlight = new PlaneFlightsEntity();
        CountryDetailsEntity countryDetails = new CountryDetailsEntity();

        country.setPlaces(List.of(place));
        country.setHotels(List.of(hotel));
        country.setPackages(List.of(packageEntity));
        country.setDepartingFlights(List.of(departingFlight));
        country.setArrivingFlights(List.of(arrivingFlight));
        country.setCountryDetails(countryDetails);

        CountryService countryService = new CountryService(countryUtils, countryDetailsUtils,
                placeUtils, hotelUtils, packageUtils, planeFlightsUtils, s3Service);

        //behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);

        //when
        ResponseEntity<?> response = countryService.deleteCountry(countryId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(placeUtils).delete(place);
        verify(hotelUtils).delete(hotel);
        verify(packageUtils).delete(packageEntity);
        verify(planeFlightsUtils).delete(departingFlight);
        verify(planeFlightsUtils).delete(arrivingFlight);
        verify(countryDetailsUtils).delete(countryDetails);
        verify(countryUtils).delete(country);
    }

}