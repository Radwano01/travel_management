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
    PackageUtils packageUtils;


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
        // given
        EditCountryDto dto = new EditCountryDto();
        dto.setCountry("New Country");

        CountryEntity country = new CountryEntity();

        // behavior
        when(countryUtils.checkHelper(dto)).thenReturn(true);
        when(countryUtils.findCountryById(1)).thenReturn(country);
        when(countryUtils.existsByCountry("New Country")).thenReturn(false);

        if (dto.getCountry() != null && !countryUtils.existsByCountry(dto.getCountry())) {
            country.setCountry(dto.getCountry());
        }

        // when
        ResponseEntity<?> response = countryService.editCountry(1, dto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("New Country", country.getCountry());
        verify(countryUtils).checkHelper(dto);
        verify(countryUtils).findCountryById(1);
        verify(countryUtils).editHelper(country, dto);
        verify(countryUtils).save(country);
    }


    @Test
    void deleteCountry() {
        //given
        CountryEntity countryEntity = new CountryEntity();
        countryEntity.setId(1);
        countryEntity.setMainImage("mainImage.jpg");

        PlaceEntity placeEntity = new PlaceEntity();
        PlaceDetailsEntity placeDetails = new PlaceDetailsEntity();
        placeEntity.setPlaceDetails(placeDetails);
        List<PlaceEntity> places = new ArrayList<>();
        places.add(placeEntity);
        countryEntity.setPlaces(places);

        HotelEntity hotelEntity = new HotelEntity();
        RoomDetailsEntity roomDetails = new RoomDetailsEntity();
        hotelEntity.setRoomDetails(roomDetails);
        List<HotelEntity> hotels = new ArrayList<>();
        hotels.add(hotelEntity);
        countryEntity.setHotels(hotels);

        PackageEntity packageEntity = new PackageEntity();
        List<PackageEntity> packages = new ArrayList<>();
        packages.add(packageEntity);
        countryEntity.setPackages(packages);

        CountryDetailsEntity countryDetails = new CountryDetailsEntity();
        countryDetails.setImageOne("imageOne.jpg");
        countryDetails.setImageTwo("imageTwo.jpg");
        countryDetails.setImageThree("imageThree.jpg");
        countryEntity.setCountryDetails(countryDetails);

        //behavior
        when(countryUtils.findCountryById(1)).thenReturn(countryEntity);

        //when
        ResponseEntity<?> response = countryService.deleteCountry(1);

        //then
        assertEquals(ResponseEntity.ok("Country and country details deleted successfully"), response);

        verify(placeDetailsUtils).delete(any(PlaceDetailsEntity.class));
        verify(placeUtils).delete(any(PlaceEntity.class));
        verify(roomDetailsUtils).delete(any(RoomDetailsEntity.class));
        verify(hotelUtils).delete(any(HotelEntity.class));verify(packageUtils).delete(any(PackageEntity.class));
        verify(countryDetailsUtils).delete(any(CountryDetailsEntity.class));
        verify(countryUtils).delete(any(CountryEntity.class));
        verify(s3Service).deleteFiles(any(String[].class));
        verify(s3Service).deleteFile("mainImage.jpg");
    }

}