package com.hackathon.backend.country.services;

import com.hackathon.backend.dto.countryDto.placeDto.EditPlaceDetailsDto;
import com.hackathon.backend.dto.countryDto.placeDto.GetPlaceDetailsDto;
import com.hackathon.backend.entities.country.PlaceDetailsEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import com.hackathon.backend.repositories.country.PlaceRepository;
import com.hackathon.backend.services.country.impl.PlaceDetailsServiceImpl;
import com.hackathon.backend.utilities.S3Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceDetailsServiceImplTest {

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private PlaceDetailsServiceImpl placeDetailsServiceImpl;


    @AfterEach
    void tearDown(){
        placeRepository.deleteAll();
    }


    @Test
    void getSinglePlaceDetails() {
        int placeId = 1;
        GetPlaceDetailsDto dto = new GetPlaceDetailsDto(
                placeId, "Place Name", "Main Image", "Image One", "Image Two", "Image Three", "Description"
        );

        when(placeRepository.findPlaceWithPlaceDetailsByPlaceId(placeId)).thenReturn(dto);

        ResponseEntity<GetPlaceDetailsDto> response = placeDetailsServiceImpl.getSinglePlaceDetails(placeId);

        assertEquals(dto, response.getBody());
    }

    @Test
    void editPlaceDetails() {
        int placeId = 1;

        // Create MultipartFile mocks
        MockMultipartFile imageOneFile = new MockMultipartFile("file", "newImageOne", "image/jpeg", "content".getBytes());
        MockMultipartFile imageTwoFile = new MockMultipartFile("file", "newImageTwo", "image/jpeg", "content".getBytes());
        MockMultipartFile imageThreeFile = new MockMultipartFile("file", "newImageThree", "image/jpeg", "content".getBytes());

        EditPlaceDetailsDto editDto = new EditPlaceDetailsDto();
        editDto.setImageOne(imageOneFile);
        editDto.setImageTwo(imageTwoFile);
        editDto.setImageThree(imageThreeFile);
        editDto.setDescription("New Description");

        PlaceEntity place = new PlaceEntity();
        place.setId(1);
        place.setMainImage("uploadedMainImage");
        PlaceDetailsEntity placeDetails = new PlaceDetailsEntity();
        place.setPlaceDetails(placeDetails);

        // Mock behavior
        when(placeRepository.findById(placeId)).thenReturn(java.util.Optional.of(place));
        when(s3Service.uploadFile(imageOneFile)).thenReturn("uploadedImageOne");
        when(s3Service.uploadFile(imageTwoFile)).thenReturn("uploadedImageTwo");
        when(s3Service.uploadFile(imageThreeFile)).thenReturn("uploadedImageThree");

        // Set existing images
        placeDetails.setImageOne("oldImageOne");
        placeDetails.setImageTwo("oldImageTwo");
        placeDetails.setImageThree("oldImageThree");

        // Call the method
        ResponseEntity<String> response = placeDetailsServiceImpl.editPlaceDetails(placeId, editDto);

        // Verify interactions
        verify(s3Service).deleteFile("oldImageOne");
        verify(s3Service).deleteFile("oldImageTwo");
        verify(s3Service).deleteFile("oldImageThree");
        verify(s3Service).uploadFile(imageOneFile);
        verify(s3Service).uploadFile(imageTwoFile);
        verify(s3Service).uploadFile(imageThreeFile);

        // Verify the response
        GetPlaceDetailsDto expectedDto = new GetPlaceDetailsDto(
                placeId, place.getPlace(), "uploadedMainImage",
                "uploadedImageOne", "uploadedImageTwo",
                "uploadedImageThree", "New Description"
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto.toString(), response.getBody());
    }

    @Test
    void editPlaceDetails_WithEmptyData() {
        //given
        int placeId = 1;
        EditPlaceDetailsDto editDto = new EditPlaceDetailsDto(); // Empty DTO

        //when
        ResponseEntity<String> response = placeDetailsServiceImpl.editPlaceDetails(placeId, editDto);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("you sent an empty data to change", response.getBody());
    }
}