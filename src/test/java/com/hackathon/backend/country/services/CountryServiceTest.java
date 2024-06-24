package com.hackathon.backend.country.services;
import com.hackathon.backend.dto.countryDto.EditCountryDto;
import com.hackathon.backend.dto.countryDto.GetCountryDto;
import com.hackathon.backend.dto.countryDto.PostCountryDto;
import com.hackathon.backend.entities.country.CountryDetailsEntity;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.entities.hotel.RoomEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import com.hackathon.backend.entities.package_.PackageEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import com.hackathon.backend.repositories.hotel.hotelFeatures.HotelFeaturesRepository;
import com.hackathon.backend.services.country.CountryService;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryDetailsUtils;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.country.PlaceDetailsUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import com.hackathon.backend.utilities.hotel.HotelEvaluationUtils;
import com.hackathon.backend.utilities.hotel.HotelUtils;
import com.hackathon.backend.utilities.hotel.RoomDetailsUtils;
import com.hackathon.backend.utilities.hotel.RoomUtils;
import com.hackathon.backend.utilities.hotel.features.HotelFeaturesUtils;
import com.hackathon.backend.utilities.hotel.features.RoomFeaturesUtils;
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
    PlaceDetailsUtils placeDetailsUtils;
    @Mock
    HotelUtils hotelUtils;

    @Mock
    RoomDetailsUtils roomDetailsUtils;

    @Mock
    HotelEvaluationUtils hotelEvaluationUtils;

    @Mock
    RoomUtils roomUtils;

    @Mock
    HotelFeaturesUtils hotelFeaturesUtils;

    @Mock
    RoomFeaturesUtils roomFeaturesUtils;

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
        PostCountryDto c = new PostCountryDto(
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
        GetCountryDto getCountryDto = new GetCountryDto();
        getCountryDto.setCountry("testCountry");
        getCountryDto.setMainImage("testImage");
        List<GetCountryDto> getCountryDtos = new ArrayList<>();

        //behavior
        when(countryUtils.findAllCountries()).thenReturn(getCountryDtos);

        //when
        ResponseEntity<?> response = countryService.getCountry();

        ///then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getCountryDtos, response.getBody());
    }

    @Test
    void editCountry() {
        //given
        int countryId = 1;
        CountryEntity country = new CountryEntity();
        country.setId(countryId);
        country.setCountry("testCountry");
        country.setMainImage("testImage");

        EditCountryDto editCountryDto = new EditCountryDto();
        editCountryDto.setCountry("testCountry1");
        editCountryDto.setMainImage(new MockMultipartFile("mainImage", "mainImage.jpg", "image/jpeg", new byte[0]));

        //behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);
        when(s3Service.uploadFile(editCountryDto.getMainImage())).thenReturn("newMainImage");

        //when
        ResponseEntity<?> response = countryService.editCountry(countryId, editCountryDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(editCountryDto.getCountry(), country.getCountry());
        assertEquals("newMainImage", country.getMainImage());
    }

    @Test
    void deleteCountry() {
        // given
        int countryId = 1;
        CountryEntity country = new CountryEntity();
        country.setId(countryId);

        PlaceEntity place = new PlaceEntity();
        PlaceDetailsEntity placeDetails = new PlaceDetailsEntity();
        place.setPlaceDetails(placeDetails);

        HotelEntity hotel = new HotelEntity();
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        HotelEvaluationEntity hotelEvaluation = new HotelEvaluationEntity();
        RoomEntity room = new RoomEntity();

        HotelFeaturesEntity hotelFeature = new HotelFeaturesEntity();
        RoomFeaturesEntity roomFeature = new RoomFeaturesEntity();

        hotel.setRoomDetails(roomDetails);
        hotel.getEvaluations().add(hotelEvaluation);
        hotel.getRooms().add(room);

        roomDetails.setHotelFeatures(List.of(hotelFeature));
        roomDetails.setRoomFeatures(List.of(roomFeature));

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
                placeUtils, placeDetailsUtils, hotelUtils, roomDetailsUtils, hotelEvaluationUtils, roomUtils,
                hotelFeaturesUtils, roomFeaturesUtils, packageUtils, planeFlightsUtils, s3Service);

        // behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);

        // when
        ResponseEntity<?> response = countryService.deleteCountry(countryId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(placeDetailsUtils).delete(placeDetails);
        verify(placeUtils).delete(place);
        verify(hotelUtils).delete(hotel);
        verify(hotelEvaluationUtils).delete(hotelEvaluation);
        verify(roomUtils).delete(room);
        verify(roomDetailsUtils).delete(roomDetails);
        verify(packageUtils).delete(packageEntity);
        verify(planeFlightsUtils).delete(departingFlight);
        verify(planeFlightsUtils).delete(arrivingFlight);
        verify(countryDetailsUtils).delete(countryDetails);
        verify(countryUtils).delete(country);
    }



}