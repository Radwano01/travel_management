package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.placeDto.CreatePlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceForFlightDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import com.hackathon.backend.repositories.country.CountryRepository;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.services.country.impl.PlaceServiceImpl;
import com.hackathon.backend.utilities.S3Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceServiceImplTest {

    @Mock
    CountryRepository countryRepository;

    @Mock
    PlaceRepository placeRepository;

    @Mock
    S3Service s3Service;

    @InjectMocks
    PlaceServiceImpl placeServiceImpl;

    CreatePlaceDto createPlaceDto;
    EditPlaceDto editPlaceDto;
    CountryEntity country;
    @Mock
    PlaceEntity place = new PlaceEntity();

    @Mock
    private HotelEntity hotel;

    @BeforeEach
    void setUp(){
        createPlaceDto = new CreatePlaceDto(
                "test place",
                new MockMultipartFile("mainImage", "newImageOne.jpg", "image/jpeg", "image content".getBytes()),
                new MockMultipartFile("imageOne", "newImageOne.jpg", "image/jpeg", "image content".getBytes()),
                new MockMultipartFile("imageOne", "newImageOne.jpg", "image/jpeg", "image content".getBytes()),
                new MockMultipartFile("imageOne", "newImageOne.jpg", "image/jpeg", "image content".getBytes()),
                "test description"
        );

        editPlaceDto = new EditPlaceDto(
                "edited test place",
                new MockMultipartFile("mainImage", "newImageOne.jpg", "image/jpeg", "image content".getBytes())
        );

        country = new CountryEntity(
                "test country",
                "test.image"
        );
        country.setId(1);

        hotel.setRoomDetails(new RoomDetailsEntity());
    }

    @AfterEach
    void tearDown(){
        countryRepository.deleteAll();
        placeRepository.deleteAll();
    }


    @Test
    void createPlace() {

        //behavior
        when(countryRepository.findById(1)).thenReturn(Optional.ofNullable(country));

        //when
        ResponseEntity<String> response = placeServiceImpl.createPlace(1, createPlaceDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAllPlacesByCountryId() {
        List<GetEssentialPlaceDto> ls = new ArrayList<>();

        //behavior
        when(countryRepository.findEssentialPlacesDataByCountryId(1)).thenReturn(ls);

        //when
        ResponseEntity<List<GetEssentialPlaceDto>> response = placeServiceImpl.getAllPlacesByCountryId(1);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ls, response.getBody());
    }

    @Test
    void getPlaceByPlace() {
        //given
        List<GetPlaceForFlightDto> ls = new ArrayList<>();

        //behavior
        when(placeRepository.findPlaceByPlace("test place")).thenReturn(ls);

        //when
        ResponseEntity<List<GetPlaceForFlightDto>> response = placeServiceImpl.getPlaceByPlace("test place");

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void editPlace_Success() {
        // given
        int countryId = 1;
        int placeId = 1;

        // behavior
        when(countryRepository.findPlaceByCountryIdANDPlaceId(anyInt(), anyInt())).thenReturn(Optional.of(place));
        when(countryRepository.findById(anyInt())).thenReturn(Optional.ofNullable(country));
        when(countryRepository.save(any(CountryEntity.class))).thenReturn(country);

        // when
        ResponseEntity<String> response = placeServiceImpl.editPlace(countryId, placeId, editPlaceDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void editPlace_EmptyData() {
        //given
        int countryId = 1;
        int placeId = 1;
        EditPlaceDto editPlaceDto = new EditPlaceDto();

        //when
        ResponseEntity<String> response = placeServiceImpl.editPlace(countryId, placeId, editPlaceDto);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deletePlace_Success() {
        //given
        int countryId = 1;
        int placeId = 1;

        RoomDetailsEntity roomDetails = mock(RoomDetailsEntity.class);
        PlaceDetailsEntity placeDetails = mock(PlaceDetailsEntity.class);

        when(roomDetails.getImageOne()).thenReturn("imageOne");
        when(roomDetails.getImageTwo()).thenReturn("imageTwo");
        when(roomDetails.getImageThree()).thenReturn("imageThree");
        when(roomDetails.getImageFour()).thenReturn("imageFour");

        when(placeDetails.getImageOne()).thenReturn("placeImageOne");
        when(placeDetails.getImageTwo()).thenReturn("placeImageTwo");
        when(placeDetails.getImageThree()).thenReturn("placeImageThree");

        // behavior
        when(countryRepository.findById(countryId)).thenReturn(Optional.ofNullable(country));
        when(countryRepository.findPlaceByCountryIdANDPlaceId(countryId, placeId)).thenReturn(Optional.ofNullable(place));
        when(place.getHotels()).thenReturn(List.of(hotel));
        when(hotel.getRoomDetails()).thenReturn(roomDetails);
        when(place.getMainImage()).thenReturn("placeMainImage");
        when(place.getPlaceDetails()).thenReturn(placeDetails);

        //when
        ResponseEntity<String> response = placeServiceImpl.deletePlace(countryId, placeId);

        //then
        verify(s3Service).deleteFile("imageOne");
        verify(s3Service).deleteFile("imageTwo");
        verify(s3Service).deleteFile("imageThree");
        verify(s3Service).deleteFile("imageFour");
        verify(s3Service).deleteFile("placeMainImage");
        verify(s3Service).deleteFile("placeImageOne");
        verify(s3Service).deleteFile("placeImageTwo");
        verify(s3Service).deleteFile("placeImageThree");
        verify(countryRepository).save(country);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Place deleted successfully", response.getBody());
    }
}