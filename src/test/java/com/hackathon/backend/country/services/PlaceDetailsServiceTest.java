package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDetailsDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceDetailsDto;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.services.country.PlaceDetailsService;
import com.hackathon.backend.utilities.amazonServices.S3Service;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceDetailsServiceTest {

    @Mock
    PlaceUtils placeUtils;

    @Mock
    PlaceDetailsUtils placeDetailsUtils;

    @Mock
    S3Service s3Service;

    @InjectMocks
    PlaceDetailsService placeDetailsService;

    @Test
    void getSinglePlaceDetails() {
        //given
        int placeId = 1;

        PlaceDetailsEntity placeDetails = new PlaceDetailsEntity();
        placeDetails.setImageOne("testImageOne");
        placeDetails.setImageTwo("testImageTwo");
        placeDetails.setImageThree("testImageThree");
        placeDetails.setDescription("testDesc");

        PlaceEntity place = new PlaceEntity();
        place.setId(placeId);
        place.setPlace("testPlace");
        place.setMainImage("testImage");
        place.setPlaceDetails(placeDetails);

        //behavior
        when(placeUtils.findById(placeId)).thenReturn(place);

        //when
        ResponseEntity<?> response = placeDetailsService.getSinglePlaceDetails(placeId);
        GetPlaceDetailsDto responseData = (GetPlaceDetailsDto) response.getBody();

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(place.getId(), responseData.getId());
        assertEquals(place.getPlace(), responseData.getPlace());
        assertEquals(place.getMainImage(), responseData.getMainImage());
        assertEquals(responseData.getImageOne(), placeDetails.getImageOne());
        assertEquals(responseData.getImageTwo(), placeDetails.getImageTwo());
        assertEquals(responseData.getImageThree(), placeDetails.getImageThree());
        assertEquals(responseData.getDescription(), placeDetails.getDescription());
    }


    @Test
    void editPlaceDetails() {
        //given
        int placeId = 1;

        PlaceDetailsEntity placeDetails = new PlaceDetailsEntity();
        placeDetails.setImageOne("testImageOne");
        placeDetails.setImageTwo("testImageTwo");
        placeDetails.setImageThree("testImageThree");
        placeDetails.setDescription("testDesc");

        PlaceEntity place = new PlaceEntity();
        place.setId(placeId);
        place.setPlace("testPlace");
        place.setMainImage("testImage");
        place.setPlaceDetails(placeDetails);

        EditPlaceDetailsDto editPlaceDetailsDto = new EditPlaceDetailsDto(
                new MockMultipartFile("imageOne", "imageOne.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageTwo", "imageTwo.jpg", "image/jpeg", new byte[0]),
                new MockMultipartFile("imageThree", "imageThree.jpg", "image/jpeg", new byte[0]),
                "testDesc1"
        );

        //behavior
        when(placeUtils.findById(placeId)).thenReturn(place);
        when(s3Service.uploadFile(editPlaceDetailsDto.getImageOne())).thenReturn("imageOne");
        when(s3Service.uploadFile(editPlaceDetailsDto.getImageTwo())).thenReturn("imageTwo");
        when(s3Service.uploadFile(editPlaceDetailsDto.getImageThree())).thenReturn("imageThree");

        //when
        ResponseEntity<?> response = placeDetailsService.editPlaceDetails(placeId, editPlaceDetailsDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("imageOne", place.getPlaceDetails().getImageOne());
        assertEquals("imageTwo", place.getPlaceDetails().getImageTwo());
        assertEquals("imageThree", place.getPlaceDetails().getImageThree());
        assertEquals("testDesc1", place.getPlaceDetails().getDescription());
    }
}