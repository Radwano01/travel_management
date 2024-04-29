//package com.hackathon.backend.country.services;
//
//import com.hackathon.backend.dto.countryDto.PlaceDto.PlaceDetailsDto;
//import com.hackathon.backend.entities.country.PlaceDetailsEntity;
//import com.hackathon.backend.entities.country.PlaceEntity;
//import com.hackathon.backend.repositories.country.PlaceDetailsRepository;
//import com.hackathon.backend.repositories.country.PlaceRepository;
//import com.hackathon.backend.services.country.PlaceDetailsService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class PlaceDetailsServiceTest {
//
//    @Mock
//    private PlaceRepository placeRepository;
//
//    @Mock
//    private PlaceDetailsRepository placeDetailsRepository;
//
//    private PlaceDetailsService placeDetailsService;
//
//    @BeforeEach
//    void setUp() {
//        placeDetailsService = new PlaceDetailsService(
//                placeRepository,
//                placeDetailsRepository);
//    }
//
//    @AfterEach
//    void tearDown() {
//        placeDetailsRepository.deleteAll();
//        placeRepository.deleteAll();
//    }
//
//    @Test
//    void getSinglePlaceDetails() {
//        //given
//        PlaceDetailsEntity placeDetails = new PlaceDetailsEntity();
//        placeDetails.setImageOne("image_one");
//        placeDetails.setImageTwo("image_two");
//        placeDetails.setImageThree("image_three");
//        placeDetails.setDescription("description");
//
//
//        PlaceEntity place = new PlaceEntity();
//        place.setId(1);
//        place.setPlace("London");
//        place.setMainImage("url_image");
//        place.setPlaceDetails(placeDetails);
//
//        when(placeRepository.findById(1)).thenReturn(Optional.of(place));
//
//        //when
//        ResponseEntity<?> response = placeDetailsService.getSinglePlaceDetails(1);
//        //then
//        assertNotNull(response.getBody());
//        PlaceDetailsDto placeDetailsDtos = (PlaceDetailsDto) response.getBody();
//        assertNotNull(placeDetailsDtos);
//
//        assertEquals(place.getId(), placeDetailsDtos.getId());
//        assertEquals(place.getPlace(), placeDetailsDtos.getPlace());
//        assertEquals(place.getMainImage(), placeDetailsDtos.getMainImage());
//        assertEquals(placeDetails.getImageOne(), placeDetailsDtos.getImageOne());
//        assertEquals(placeDetails.getImageTwo(), placeDetailsDtos.getImageTwo());
//        assertEquals(placeDetails.getImageThree(), placeDetailsDtos.getImageThree());
//        assertEquals(placeDetails.getDescription(), placeDetailsDtos.getDescription());
//
//    }
//
//    @Test
//    void editPlaceDetails() {
//        //given
//        int placeDetailsId = 1;
//        PlaceDetailsDto placeDetailsDto = new PlaceDetailsDto();
//        placeDetailsDto.setId(1);
//        placeDetailsDto.setImageOne("image_one");
//        placeDetailsDto.setImageTwo("image_two");
//        placeDetailsDto.setImageThree("image_three");
//        placeDetailsDto.setDescription("description");
//        placeDetailsDto.setPlace("London");
//        when(placeRepository.findByPlace(placeDetailsDto.getPlace().trim()))
//                .thenReturn(Optional.of(new PlaceEntity(placeDetailsDto.getPlace().trim())));
//        when(placeDetailsRepository.findById(placeDetailsId)).thenReturn(Optional.of(new PlaceDetailsEntity()));
//
//        //when
//        ResponseEntity<?> response = placeDetailsService.editPlaceDetails(placeDetailsId, placeDetailsDto);
//
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//}