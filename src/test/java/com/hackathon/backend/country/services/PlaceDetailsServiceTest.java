package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.placeDto.PlaceDetailsDto;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.services.country.PlaceDetailsService;
import com.hackathon.backend.utilities.country.PlaceDetailsUtils;
import com.hackathon.backend.utilities.country.PlaceUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceDetailsServiceTest {

    @Mock
    private PlaceUtils placeUtils;

    @Mock
    private PlaceDetailsUtils placeDetailsUtils;

    private PlaceDetailsService placeDetailsService;

    @BeforeEach
    void setUp() {
        placeDetailsService = new PlaceDetailsService(
                placeUtils,
                placeDetailsUtils);
    }

    @AfterEach
    void tearDown() {
        placeUtils.deleteAll();
        placeDetailsUtils.deleteAll();
    }

    @Test
    void getSinglePlaceDetails() {
        //given
        PlaceDetailsEntity placeDetails = new PlaceDetailsEntity();
        placeDetails.setImageOne("image_one");
        placeDetails.setImageTwo("image_two");
        placeDetails.setImageThree("image_three");
        placeDetails.setDescription("description");


        PlaceEntity place = new PlaceEntity();
        place.setId(1);
        place.setPlace("London");
        place.setMainImage("url_image");
        place.setPlaceDetails(placeDetails);

        when(placeUtils.findById(1)).thenReturn(place);

        //when
        ResponseEntity<?> response = placeDetailsService.getSinglePlaceDetails(1);
        //then
        assertNotNull(response.getBody());
        PlaceDetailsDto placeDetailsDtos = (PlaceDetailsDto) response.getBody();
        assertNotNull(placeDetailsDtos);

        assertEquals(place.getId(), placeDetailsDtos.getId());
        assertEquals(place.getPlace(), placeDetailsDtos.getPlace());
        assertEquals(place.getMainImage(), placeDetailsDtos.getMainImage());
        assertEquals(placeDetails.getImageOne(), placeDetailsDtos.getImageOne());
        assertEquals(placeDetails.getImageTwo(), placeDetailsDtos.getImageTwo());
        assertEquals(placeDetails.getImageThree(), placeDetailsDtos.getImageThree());
        assertEquals(placeDetails.getDescription(), placeDetailsDtos.getDescription());

    }

    @Test
    void editPlaceDetails() {
        //given
        int placeDetailsId = 1;
        PlaceDetailsDto placeDetailsDto = new PlaceDetailsDto();
        placeDetailsDto.setId(1);
        placeDetailsDto.setImageOne("image_one");
        placeDetailsDto.setImageTwo("image_two");
        placeDetailsDto.setImageThree("image_three");
        placeDetailsDto.setDescription("description");
        placeDetailsDto.setPlace("London");

        when(placeDetailsUtils.findById(placeDetailsId)).thenReturn(new PlaceDetailsEntity());

        //when
        ResponseEntity<?> response = placeDetailsService.editPlaceDetails(placeDetailsId, placeDetailsDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}