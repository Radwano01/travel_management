package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetEssentialPlaceDto;
import com.hackathon.backend.dto.countryDto.placeDto.PostPlaceDto;
import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.services.country.PlaceService;
import com.hackathon.backend.utilities.amazonServices.S3Service;
import com.hackathon.backend.utilities.country.CountryUtils;
import com.hackathon.backend.utilities.country.PlaceDetailsUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @Mock
    CountryUtils countryUtils;

    @Mock
    PlaceUtils placeUtils;

    @Mock
    PlaceDetailsUtils placeDetailsUtils;

    @Mock
    S3Service s3Service;

    @InjectMocks
    PlaceService placeService;

    @Test
    void createPlace() {
        // given
        int countryId = 1;

        PostPlaceDto p = new PostPlaceDto(
                "testPlace",
                new MockMultipartFile("mainImage", "mainImage.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageOne", "imageOne.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageTwo", "imageTwo.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageThree", "imageThree.jpg", "image/jpeg", new byte[0]),
                "testDesc"
        );

        CountryEntity country = new CountryEntity();
        country.setId(countryId);
        country.setCountry("testCountry");
        country.setMainImage("testImage");

        // Mock the behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);

        // when
        ResponseEntity<?> response = placeService.createPlace(countryId, p);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void getPlacesByCountry() {
        //given
        int countryId = 1;
        CountryEntity country = new CountryEntity();
        country.setId(countryId);
        country.setCountry("testCountry");
        country.setMainImage("testImage");

        PlaceEntity place = new PlaceEntity();
        place.setId(1);
        place.setPlace("testImage");
        place.setMainImage("testImage");
        place.setCountry(country);

        country.getPlaces().add(place);

        GetEssentialPlaceDto getEssentialPlaceDto = new GetEssentialPlaceDto();
        getEssentialPlaceDto.setId(place.getId());
        getEssentialPlaceDto.setPlace(place.getPlace());
        getEssentialPlaceDto.setMainImage(place.getMainImage());

        List<GetEssentialPlaceDto> res = new ArrayList<>();
        res.add(getEssentialPlaceDto);

        //behavior
        when(placeUtils.findPlacesByCountryId(countryId)).thenReturn(res);

        //when
        ResponseEntity<?> response = placeService.getPlacesByCountry(countryId);

        List<GetEssentialPlaceDto> placeEntities = (List<GetEssentialPlaceDto>) response.getBody();
        GetEssentialPlaceDto responseData = placeEntities.get(0);

        //then

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(place.getId(), responseData.getId());
        assertEquals(place.getPlace(), responseData.getPlace());
        assertEquals(place.getMainImage(), responseData.getMainImage());
    }

    @Test
    void editPlace() {
        //given
        int countryId = 1;
        int placeId = 1;
        EditPlaceDto editPlaceDto = new EditPlaceDto(
                "testPlace",
                new MockMultipartFile("mainImage", "mainImage.jpg", "image/jpeg", new byte[0])
        );

        PlaceEntity place = new PlaceEntity();
        place.setId(placeId);

        CountryEntity country = new CountryEntity();
        country.setId(countryId);
        country.getPlaces().add(place);

        //behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);
        when(s3Service.uploadFile(editPlaceDto.getMainImage())).thenReturn("mainImage");

        //when
        ResponseEntity<?> response = placeService.editPlace(countryId, placeId, editPlaceDto);

        //then
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testPlace", place.getPlace());
        assertEquals("mainImage", place.getMainImage());
    }

    @Test
    void deletePlace() {
        //given
        int countryId = 1;
        int placeId = 1;

        PlaceDetailsEntity placeDetails = new PlaceDetailsEntity();

        PlaceEntity place = new PlaceEntity();
        place.setId(placeId);
        place.setMainImage("testImage");
        place.setPlaceDetails(placeDetails);

        CountryEntity country = new CountryEntity();
        country.setId(countryId);
        country.getPlaces().add(place);

        //behavior
        when(countryUtils.findCountryById(countryId)).thenReturn(country);

        //when
        ResponseEntity<?> response = placeService.deletePlace(countryId,placeId);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(placeDetailsUtils).delete(placeDetails);
        verify(placeUtils).delete(place);
        verify(s3Service).deleteFile(place.getMainImage());
    }
}